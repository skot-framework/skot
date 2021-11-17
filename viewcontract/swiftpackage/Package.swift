// swift-tools-version:5.5
import PackageDescription

let package = Package(
    name: "SkotViewContract",
    platforms: [
        .iOS(.v15)
    ],
    products: [
        .library(
            name: "SkotViewContract",
            targets: ["SkotViewContract"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "SkotViewContract",
            path: "./SkotViewContract.xcframework"
        ),
    ]
)
