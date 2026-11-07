#!/usr/bin/env bash
# Builds dist/pane-seam-fix-<version>.jar with plain javac (JDK 17+), no Gradle or downloads.
# The classes under stubs/ stand in for Minecraft and Mixin at compile time only and are never
# packaged; at runtime Fabric Loader supplies the real ones (Minecraft 26.x is unobfuscated, so
# the names match). Tests in test/ run the real mixin handler against a copy of 26.3's culling logic.
set -euo pipefail
cd "$(dirname "$0")"

VERSION="1.1.0"
JAR="dist/pane-seam-fix-${VERSION}.jar"

rm -rf build
mkdir -p build/stubs build/classes build/test build/jar dist

echo "Compiling stubs..."
javac --release 17 -d build/stubs $(find stubs -name '*.java')

echo "Compiling mod..."
javac --release 17 -Xlint:all -cp build/stubs -d build/classes $(find src/main/java -name '*.java')

echo "Running tests..."
javac --release 17 -cp build/stubs:build/classes -d build/test $(find test -name '*.java')
java -cp build/stubs:build/classes:build/test paneseamfix.PaneSeamTest

echo "Packaging..."
cp -r build/classes/. build/jar/
cp src/main/resources/* build/jar/
cp LICENSE build/jar/LICENSE_pane_seam_fix
printf "Manifest-Version: 1.0\nImplementation-Title: Pane Seam Fix\nImplementation-Version: %s\n" "$VERSION" > build/MANIFEST.MF
rm -f "$JAR"
(cd build/jar && jar --create --file "../../$JAR" --manifest ../MANIFEST.MF .)

echo "Built $JAR"
