# DexKiller
Parse classes.dex in APK file and get strings/methods/class info. no need unzip, no need re-compile.

##usage
```
String apkPath = "path/to/apk/file";
List<Dex> dexResult = DexParser.parseEachDexFile(apkPath);
```
All the dex info is stored in the dexResult, such as classList/methodList/stringList.


