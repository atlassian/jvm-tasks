# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## SemVer public API
The library offers compatibility contracts on the Java API and the POM.

### Java API
The API consists of all public Kotlin types from `com.atlassian.performance.tools.jvmtasks.api` and its subpackages:

  * [source compatibility]
  * [binary compatibility]
  * [behavioral compatibility] with behavioral contracts expressed via Javadoc

[source compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#source_compatibility
[binary compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#binary_compatibility
[behavioral compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#behavioral_compatibility

### POM
Changing the license is breaking a contract.
Adding a requirement of a major version of a dependency is breaking a contract.
Dropping a requirement of a major version of a dependency is a new contract.

## [Unreleased]
[Unreleased]: https://github.com/atlassian/jvm-tasks/compare/release-1.3.0...master

## [1.4.1] - 2024-06-14
[1.4.1]: https://github.com/atlassian/jvm-tasks/compare/release-1.4.0...release-1.4.1

### Fixed
- Fix missing `net.jcip:jcip-annotations` license.

## [1.4.0] - 2023-12-22
[1.4.0]: https://github.com/atlassian/jvm-tasks/compare/release-1.3.0...release-1.4.0

### Added
- Add `EventBus` for pub-sub loose coupling between layers. E.g. for GUI live-reacting to events from deep layers.
- Add `TaskScope` to scope out JVM subtasks and publish task events.

### Deprecated
- Deprecate `TaskTimer` in favor of `TaskScope`.
  The scope is more generic. It doesn't log task time, but it publishes events. Event subs can measure the task time and
  do more powerful things than just logging.

### Fixed
- Use `TaskScope` within `TaskTimer`.

## [1.3.0] - 2023-07-26
[1.3.0]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.4...release-1.3.0

This change in POM was already done in [1.2.4]. This release upgrades the change into a contract.
E.g. you can depend on `[1.3.0, 2.0.0)` and know that `log4j-core` will not come back and cause a conflict.

### Added
- Drop major versions of `log4j-core` and `log4j-slf4j-impl`. Fix [JPERF-570].
- Add `TaskTimer.TaskStartedHandler`, `TaskTimer.TaskSucceededHandler` and `TaskTimer.TaskFailedHandler` interfaces. Unblock [JPERF-1196].
- Add functions for subscribing and unsubscribing task handlers in `TaskTimer`. Unblock [JPERF-1196].

[JPERF-1196]: https://ecosystem.atlassian.net/browse/JPERF-1196

### Fixed
- Relax `log4j-api` dependency to a SemVer range.

## [1.2.4] - 2023-03-21
[1.2.4]: https://github.com/atlassian/jvm-tasks/compare/release-1.2.3...release-1.2.4

### Fixed
- Drop `log4j-core` and `log4j-slf4j-impl` from POM. Fix [JPERF-570].
- Demote `log4j-api` from `compile` to `runtime` scope in POM. Fix [JPERF-570].

[JPERF-570]: https://ecosystem.atlassian.net/browse/JPERF-570

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
