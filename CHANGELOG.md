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
[Unreleased]: https://bitbucket.org/atlassian/jvm-tasks/branches/compare/master%0Drelease-1.0.0

### Fixed
- Log last exception from `IdempotentAction`. Fix [JPERF-461].
- Switch to a non-deprecated Kotin stdlib. Unblock [JPERF-466].

[JPERF-461]: https://ecosystem.atlassian.net/browse/JPERF-461
[JPERF-466]: https://ecosystem.atlassian.net/browse/JPERF-466

## [1.0.0] - 2018-08-29
[1.0.0]: https://bitbucket.org/atlassian/jvm-tasks/branches/compare/release-1.0.0%0Drelease-0.0.1

### Changed
- Define public API.

### Added
- License.
- Add this change log.

## [0.0.1] - 2018-07-27
[0.0.1]: https://bitbucket.org/atlassian/jvm-tasks/branches/compare/release-0.0.1%0Dinitial-commit

### Added
- Migrate JVM task management from [JPT submodule].
- Add [README.md](README.md).
- Configure Bitbucket Pipelines.

[JPT submodule]: https://stash.atlassian.com/projects/JIRASERVER/repos/jira-performance-tests/browse/tasks?at=da8bbed5b0a4b3014aa9207ffa8b7263a93a7b16
