# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## API
The API consists of all public Kotlin types from `com.atlassian.performance.tools.jvmtasks.api` and its subpackages:

  * [source compatibility]
  * [binary compatibility]
  * [behavioral compatibility] with behavioral contracts expressed via Javadoc

[source compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#source_compatibility
[binary compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#binary_compatibility
[behavioral compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#behavioral_compatibility

## [Unreleased]
[Unreleased]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.3...master

## [1.2.3] - 2022-06-29
[1.2.3]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.2...release-1.2.3

Empty release to test changes in release process.

## [1.2.2] - 2022-01-14
[1.2.2]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.1...release-1.2.2

### Fixed
- Bump log4j to `2.17.1`. Address [JPERF-767].

[JPERF-767]: https://ecosystem.atlassian.net/browse/JPERF-767

## [1.2.1] - 2020-11-12
[1.2.1]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.0...release-1.2.1

### Fixed
- Stop sleeping after last failed attempt in `IdempotentAction`. Fix [JPERF-620].

[JPERF-620]: https://ecosystem.atlassian.net/browse/JPERF-620

## [1.2.0] - 2020-11-04
[1.2.0]: https://github.com/atlassian/jvm-tasks/compare/release-1.1.0...release-1.2.0

### Added
- Parametrize `JitterBackoff` with a `Random` for predictability (seeding).

### Fixed
- Fix jitter back-offs returning negative time. Fix [JPERF-683].

[JPERF-683]: https://ecosystem.atlassian.net/browse/JPERF-683

## [1.1.0] - 2020-08-25
[1.1.0]: https://github.com/atlassian/jvm-tasks/compare/release-1.0.1...release-1.1.0

### Added
- Add static, jitter and sum back-offs.

## [1.0.1] - 2019-04-23
[1.0.1]: https://github.com/atlassian/jvm-tasks/compare/release-1.0.0...release-1.0.1

### Fixed
- Log last exception from `IdempotentAction`. Fix [JPERF-461].
- Switch to a non-deprecated Kotin stdlib. Unblock [JPERF-466].

[JPERF-461]: https://ecosystem.atlassian.net/browse/JPERF-461
[JPERF-466]: https://ecosystem.atlassian.net/browse/JPERF-466

## [1.0.0] - 2018-08-29
[1.0.0]: https://github.com/atlassian/jvm-tasks/compare/release-0.0.1...release-1.0.0

### Changed
- Define public API.

### Added
- License.
- Add this change log.

## [0.0.1] - 2018-07-27
[0.0.1]: https://github.com/atlassian/jvm-tasks/compare/initial-commit...release-0.0.1

### Added
- Migrate JVM task management from [JPT submodule].
- Add [README.md](README.md).
- Configure Bitbucket Pipelines.

[JPT submodule]: https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/tasks?at=da8bbed5b0a4b3014aa9207ffa8b7263a93a7b16
