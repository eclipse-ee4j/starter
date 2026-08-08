#!/usr/bin/env python3
"""Fetch the current jakarta.ee topbar and footer as plain HTML.

Run this whenever jakarta.ee ships a redesign to keep the starter's site
branding in sync. Writes ui/src/main/webapp/site-branding/{topbar,footer}.html.

Requires: html5lib, lxml. Example setup:
    python3 -m venv .venv && . .venv/bin/activate && pip install html5lib lxml
"""

from __future__ import annotations

import pathlib
import re
import sys
import urllib.request

import html5lib
from lxml import etree

SOURCE_URL = "https://jakarta.ee/"
OUT_DIR = pathlib.Path(__file__).resolve().parent.parent / "src" / "main" / "webapp" / "site-branding"

TOPBAR_MARKERS = ("toolbar-container-wrapper", "navbar-wrapper", "header-row", "Become a member")
FOOTER_MARKERS = ("solstice-footer", "footer-eclipse-foundation", "footer-useful-links",
                  "footer-more", "footer-newsletter", "scrollup")


def absolutize(html: str) -> str:
    return re.sub(r'\b(href|src|action|data-[a-z-]+)="/(?!/)', r'\1="https://jakarta.ee/', html)


def to_html(elem) -> str:
    return etree.tostring(elem, method="html", encoding="unicode", with_tail=False)


def extract(source_html: str) -> tuple[str, str]:
    tree = html5lib.parse(source_html, treebuilder="lxml", namespaceHTMLElements=False)
    body = tree.getroot().find(".//body")

    toolbar = next(c for c in body
                   if getattr(c, "tag", None) == "div"
                   and "toolbar-container-wrapper" in (c.get("class") or ""))
    navbar = next(c for c in body
                  if getattr(c, "tag", None) == "div"
                  and "navbar-wrapper" in (c.get("class") or "")
                  and "is-top" in (c.get("class") or ""))
    footer = next(c for c in body
                  if getattr(c, "tag", None) == "footer"
                  and c.get("id") == "solstice-footer")

    topbar_html = absolutize(to_html(toolbar) + "\n" + to_html(navbar) + "\n")
    footer_html = absolutize(to_html(footer) + "\n")
    return topbar_html, footer_html


def verify(name: str, html: str, markers: tuple[str, ...]) -> None:
    missing = [m for m in markers if m not in html]
    if missing:
        raise SystemExit(f"{name}: missing expected markers {missing} — jakarta.ee layout may have changed.")


def main() -> None:
    print(f"Fetching {SOURCE_URL} ...", file=sys.stderr)
    with urllib.request.urlopen(SOURCE_URL) as response:
        source_html = response.read().decode("utf-8")

    topbar_html, footer_html = extract(source_html)
    verify("topbar", topbar_html, TOPBAR_MARKERS)
    verify("footer", footer_html, FOOTER_MARKERS)

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    (OUT_DIR / "topbar.html").write_text(topbar_html, encoding="utf-8")
    (OUT_DIR / "footer.html").write_text(footer_html, encoding="utf-8")
    print(f"Wrote {OUT_DIR / 'topbar.html'} ({len(topbar_html)} bytes)")
    print(f"Wrote {OUT_DIR / 'footer.html'} ({len(footer_html)} bytes)")


if __name__ == "__main__":
    main()
