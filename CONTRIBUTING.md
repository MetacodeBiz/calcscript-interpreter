# Contributing

Thank you for considering a contribution to this project!

All changes will need to:

  - build successfully with `./gradlew`,
  - be signed-off,
  - have good commit messages.

Additionally changes that are not backwards-compatible (for example
changing or removing API) need to first be discussed using issues.

## Build

`./gradlew` will perform all necessary checks including test coverage and static code analysis.

## Developer Certificate of Origin

The sign-off is a simple line at the end of the git commit message,
which certifies that you wrote it or otherwise have the right to
pass it on as a open-source patch.

The rules are pretty simple: if you can [certify the below][DCO]:

```
Developer's Certificate of Origin 1.1

By making a contribution to this project, I certify that:

(a) The contribution was created in whole or in part by me and I
    have the right to submit it under the open source license
    indicated in the file; or

(b) The contribution is based upon previous work that, to the best
    of my knowledge, is covered under an appropriate open source
    license and I have the right under that license to submit that
    work with modifications, whether created in whole or in part
    by me, under the same open source license (unless I am
    permitted to submit under a different license), as indicated
    in the file; or

(c) The contribution was provided directly to me by some other
    person who certified (a), (b) or (c) and I have not modified
    it.

(d) I understand and agree that this project and the contribution
    are public and that a record of the contribution (including all
    personal information I submit with it, including my sign-off) is
    maintained indefinitely and may be redistributed consistent with
    this project or the open source license(s) involved.
```

then you just add a line saying

    Signed-off-by: Random J Developer <random@developer.example.org>

using your real name (sorry, no pseudonyms or anonymous contributions.)

If you set your `user.name` and `user.email` git configs, you can sign your
commit automatically with [`git commit --signoff`][GSO].

To sign-off your last commit:

    git commit --amend --signoff

[DCO]: https://developercertificate.org
[GSO]: https://git-scm.com/docs/git-commit#git-commit---signoff

## Commit messages

Commit messages should be self-contained and describe the motivation for a
change. The subject line should be short, with any elaborations in the body.
If the commit closes an issue, write `Closes #issuenumber` at the end of the
body.

Keep in mind [the seven rules of a great Git commit message][SR]:

  1. Separate subject from body with a blank line
  2. Limit the subject line to 50 characters
  3. Capitalize the subject line
  4. Do not end the subject line with a period
  5. Use the imperative mood in the subject line
  6. Wrap the body at 72 characters
  7. Use the body to explain what and why vs. how

[SR]: https://chris.beams.io/posts/git-commit/
