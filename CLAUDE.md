# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

`irurueta-statistics` is a small, dependency-free Java 17 library (Maven, `com.irurueta:irurueta-statistics`) providing pseudo-random number generation and common statistical distributions. All production code lives under the single package `com.irurueta.statistics` in `src/main/java`; tests mirror it 1:1 in `src/test/java` (one `*Test.java` per class, JUnit 5).

## Common commands

```bash
mvn test                                   # run the full unit test suite
mvn test -Dtest=ChiSqDistTest              # run a single test class
mvn test -Dtest=ChiSqDistTest#testCdf      # run a single test method
mvn package                                # build the jar, runs JaCoCo agent
mvn clean jacoco:prepare-agent test jacoco:report   # generate coverage report (target/site/jacoco)
mvn clean compile checkstyle:checkstyle pmd:pmd spotbugs:spotbugs  # static analysis (Checkstyle/PMD/SpotBugs)
mvn site                                   # full Maven site (javadoc, coverage, static analysis reports)
```

Checkstyle config is `checkstyle.xml` at the repo root (120-char line limit, mandatory package-info Javadoc, no tabs, etc.) — reports go to `target/checkstyle-result.xml`, `target/pmd.xml`, `target/spotbugsXml.xml`.

Antora documentation source lives in `docs/modules/ROOT`; build it locally with:

```bash
cd docs && npx antora antora-playbook.yml
```

## Architecture

The library is organized around two independent concerns that share the same special-function building blocks:

**Randomizers** (`Randomizer` abstract class → `UniformRandomizer`, `GaussianRandomizer`): wrap a `java.util.Random` (or `SecureRandom`) instance and expose typed `next*`/`fill`/`next*s` generation methods. `Randomizer.create(...)` is a factory that dispatches on `RandomizerType` to build either subclass; `GaussianRandomizer` layers mean/standard-deviation parameters on top of `Random.nextGaussian()`, while `UniformRandomizer` delegates directly to the underlying `Random`/`SecureRandom` methods.

**Distributions and special functions**: `NormalDist` and `ChiSqDist` each expose the same trio of operations — `p` (p.d.f.), `cdf`, `invcdf` — both as static methods (stateless, parameters passed explicitly) and as instance methods (state = distribution parameters set in the constructor/setters). Both distributions are implemented in terms of lower-level special functions rather than computing series directly:
- `NormalDist` delegates to `Erf` (error function / inverse error function) for `cdf`/`invcdf`.
- `ChiSqDist` delegates to `Gamma` (incomplete gamma function `gammp`/inverse `invgammp`) for `cdf`/`invcdf`, and holds a per-instance `Gamma` object to reuse across calls (`Gamma` caches `gln` and factorial tables internally, so reuse matters for performance).
- `Gamma extends GaussLegendreQuadrature`, inheriting the quadrature node/weight tables (`Y`, `W`, `N_GAU`) used to approximate the incomplete gamma function for large arguments (`ASWITCH` threshold).

All of this traces back to algorithms in *Numerical Recipes, 3rd Edition* (each class's Javadoc cites the specific section, e.g. §6.14.1 for `NormalDist`, §6.14.8 for `ChiSqDist`, §6.2.2 for `Erf`) — when modifying the numerical algorithms, cross-check against that reference rather than re-deriving from scratch.

`NormalDist.propagate(...)` implements non-linear propagation of Gaussian uncertainty through an arbitrary differentiable 1D function via a `DerivativeEvaluator` callback interface (evaluate value + derivative at the mean), following a first-order Taylor/Jacobian approximation (ported from `propagateUncertainty.m`, referenced in the Javadoc).

Numerically unstable inputs to the gamma/incomplete-gamma routines can fail to converge; this surfaces as a checked `MaxIterationsExceededException` (extends `StatisticsException`, the package's base checked exception) from `ChiSqDist.cdf`/`invcdf` and `Gamma.gammp`/`gammq`/`invgammp` — callers of chi-squared distribution methods must handle or propagate it.

`BuildInfo` is a singleton (soft-referenced) that reads `build-info.properties`, a resource generated at build time by the `groovy-maven-plugin` execution in `pom.xml` (captures version/commit/branch from CI env vars); it has no bearing on the statistical/randomizer logic.
