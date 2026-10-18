// swift-tools-version: 6.0
import PackageDescription

let package = Package(
    name: "SwiftTestHarness",
    platforms: [
        .macOS(.v15),
    ],
    dependencies: [
        .package(name: "Codex", path: "../build/SPMPackage/macosArm64/Debug")
    ],
    targets: [
        .testTarget(
            name: "SwiftTestHarnessTests",
            dependencies: [
                .product(name: "CodexLibrary", package: "Codex")
            ],
            linkerSettings: [
                .unsafeFlags([
                    "-L", "../build/SPMBuild/macosArm64/Debug/dd-a-files",
                    "-L", "../build/swift-test",
                    "-L", "../build/MergedLibraries/macos/Debug",
                    "-lCodexLibrary",
                ]),
            ]
        ),
    ]
)
