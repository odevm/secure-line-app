# Conventional Commits & SemVer

SecureLine uses [Conventional Commits](https://www.conventionalcommits.org/) and
[Semantic Versioning](https://semver.org/).

## Commit message format

```
<type>(optional scope): <description>

[optional body]

[optional footer(s)]
```

### Types

| Type | Use when |
|------|----------|
| `feat` | New user-facing capability |
| `fix` | Bug fix |
| `docs` | Documentation only |
| `chore` | Tooling, ignore files, meta, deps without behavior change |
| `refactor` | Internal change, no behavior change |
| `test` | Adding or updating tests |
| `build` | Gradle / CI build system |
| `ci` | CI configuration |
| `perf` | Performance improvement |

### Scopes (examples)

`crypto`, `modem`, `sms`, `ui`, `identity`, `backup`, `firebase`, `native`, `ci`

### Examples

```
chore: add Android and OSS gitignore
docs: add contributing and security policy
feat(ui): add Compose shell and navigation host
fix(modem): recover sync after frame CRC failure
```

## Breaking changes

Before `1.0.0`, breaking changes may land in minor bumps. Still mark them:

```
feat(crypto)!: rotate session nonce layout

BREAKING CHANGE: peers on 0.x cannot decrypt new sessions.
```

## Changelog

- User-visible changes → update `[Unreleased]` in `CHANGELOG.md`
- Tag releases as `vMAJOR.MINOR.PATCH` and move Unreleased items under that version

## Mapping to plan tasks

Prefer one logical commit (or small PR) per plan task `t01`–`t32` when practical.
