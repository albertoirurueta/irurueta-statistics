# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.4.0] - 2026-07-05

### Added

- Antora-based documentation site under `docs/`, covering installation, random generation, distributions, and the
  underlying gamma-function, error-function, chi-squared-distribution, and normal-distribution algorithms.

### Changed

- Overhauled `README.md` and updated the GitHub Actions workflow definitions. This release contains no changes to
  library source or tests.

## [1.3.4] - 2025-09-18

### Changed

- Updated GitHub Actions workflow definitions, `README.md`, and `mvnsettings.xml`. CI/tooling only, no source
  changes.

## [1.3.3] - 2025-09-17

### Changed

- Removed the now-unused Sonatype OSSRH snapshot repository/`distributionManagement` configuration from `pom.xml`
  and bumped the JUnit Jupiter test dependency to 5.13.4.

## [1.3.2] - 2024-10-18

### Changed

- Explicitly scoped the JUnit Jupiter dependency to `test`, and refactored `BuildInfo`'s internal fields and
  resource-loading code (dropped the `m` field-name prefix, adopted `var`). No behavior change.

## [1.3.1] - 2024-10-13

### Changed

- Release housekeeping only (version bump and workflow/README tweaks); no source changes.

## [1.3.0] - 2024-10-13

### Added

- New `GaussianRandomizer(double mean, double standardDeviation)` convenience constructor.

### Changed

- Migrated the build from Java 7 to Java 17, and the test suite from JUnit 4/Hamcrest to JUnit 5.
- Modernized code style throughout the package: adopted `var` type inference and dropped the Hungarian-notation
  `m` field-name prefix convention.

## [1.2.0] - 2023-11-12

### Changed

- Fixed SonarLint-flagged Javadoc and style issues across `ChiSqDist`, `Erf`, `Gamma`, `GaussianRandomizer`,
  `Randomizer`, `RandomizerType`, and their tests. Javadoc/formatting only, no behavior change.
- Added a `manual_develop.yml` CI workflow.

## [1.1.0] - 2021-12-10

### Changed

- Migrated the CI pipeline to GitHub Actions.
- Tightened the relative-error tolerance in `GaussianRandomizerTest` to fix an intermittent test failure.
- Updated `README.md`.

## [1.0.0] - 2021-12-07

Initial release.

### Added

- `UniformRandomizer` and `GaussianRandomizer` for generating uniformly and Gaussian-distributed pseudo-random
  booleans, ints, longs, floats, and doubles, built on the `Randomizer` abstraction and `RandomizerType`.
- `NormalDist` and `ChiSqDist` distribution classes exposing p.d.f. (`p`), c.d.f. (`cdf`), and inverse c.d.f.
  (`invcdf`), both as static and instance methods.
- `Gamma` and `Erf` special functions (factorials, beta function, incomplete gamma function and its inverse,
  error function and its inverse) underlying the distribution classes.
- `NormalDist.propagate` for non-linear propagation of Gaussian uncertainty through an arbitrary differentiable
  function.
- `MaxIterationsExceededException` / `StatisticsException` for convergence failures in the numerical routines.
- `BuildInfo` singleton exposing build metadata (version, commit, branch) embedded at build time.

[Unreleased]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.4.0...HEAD
[1.4.0]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.3.4...1.4.0
[1.3.4]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.3.3...1.3.4
[1.3.3]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.3.2...1.3.3
[1.3.2]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.3.1...1.3.2
[1.3.1]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.2.0...1.3.0
[1.2.0]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/albertoirurueta/irurueta-statistics/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/albertoirurueta/irurueta-statistics/releases/tag/1.0.0
