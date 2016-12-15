# DexKiller
Parse classes.dex in APK file and get strings/methods/class info. no need unzip, no need re-compile.

##usage
```
String apkPath = "path/to/apk/file";
Dex dexResult = DexParser.parseEachDexFile(apkPath);
System.out.println(dexResult.classList.size());
System.out.println(dexResult.methodList.size());
```
All the dex info is stored in the dexResult, such as classList/methodList/stringList.


