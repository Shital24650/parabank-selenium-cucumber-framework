#!/usr/bin/env python3
"""Summarise a Cucumber JSON report as a Markdown table: totals, pass rate, duration, per-tag results.

Usage: python3 scripts/metrics.py target/cucumber.json
"""
import json
import sys
from collections import defaultdict


def scenario_status(steps):
    statuses = {s.get("result", {}).get("status", "skipped") for s in steps}
    if "failed" in statuses:
        return "failed"
    if statuses <= {"skipped", "pending", "undefined"}:
        return "skipped"
    return "passed"


def main(path):
    with open(path) as f:
        features = json.load(f)

    total = defaultdict(int)
    by_tag = defaultdict(lambda: defaultdict(int))
    duration_ns = 0
    failures = []

    for feature in features:
        for element in feature.get("elements", []):
            if element.get("type") != "scenario":
                continue  # skip Background blocks
            steps = element.get("steps", [])
            status = scenario_status(steps)
            total[status] += 1
            duration_ns += sum(s.get("result", {}).get("duration", 0) for s in steps)
            for tag in element.get("tags", []):
                by_tag[tag["name"]][status] += 1
            if status == "failed":
                failures.append(element.get("name", "unnamed"))

    run = sum(total.values())
    executed = total["passed"] + total["failed"]
    pass_rate = (100.0 * total["passed"] / executed) if executed else 0.0

    print("## Test metrics\n")
    print("| Metric | Value |\n|---|---|")
    print(f"| Scenarios | {run} |")
    print(f"| Passed | {total['passed']} |")
    print(f"| Failed | {total['failed']} |")
    print(f"| Skipped | {total['skipped']} |")
    print(f"| Pass rate | {pass_rate:.1f}% |")
    print(f"| Execution time | {duration_ns / 1e9:.1f}s |")

    print("\n| Tag | Passed | Failed | Skipped |\n|---|---|---|---|")
    for tag in sorted(by_tag):
        c = by_tag[tag]
        print(f"| {tag} | {c['passed']} | {c['failed']} | {c['skipped']} |")

    if failures:
        print("\n**Failed scenarios:** " + "; ".join(failures))
    return 1 if total["failed"] else 0


if __name__ == "__main__":
    if len(sys.argv) != 2:
        sys.exit("usage: metrics.py <cucumber.json>")
    sys.exit(main(sys.argv[1]))
