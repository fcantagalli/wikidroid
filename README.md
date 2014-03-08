# WikiDroid

## Team

* Joshua Bowman (jpbowman@purdue.edu)
* Felipe Cantagalli (fcantaga@purdue.edu)
* Kriti Kochar (kkochar@purdue.edu)
* Kaiwen Xu (xu227@purdue.edu)
* Elliott Mantock (jmantock@purdue.edu)

## Requirement

* Android 4.0 (API 14)
* JRE 1.6 _(required by Android SDK)_

## Prerequisites

* Download and install JRE 1.6
	* Windows / Linux version can be downloaded [here](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html#jre-6u45-oth-JPR)
	* Mac version can be downloaded [here](http://support.apple.com/kb/DL1572)
* Download and install [Android SDK](https://developer.android.com/sdk/index.html) for your platform
	* Instructions can be found [here](https://developer.android.com/sdk/installing/index.html)

## (Optional) Emulator setup

1. Launch Android SDK Manager, and install
	1. Android 4.4.2 (API 19)
		1. ARM EABI v7a System Image
2. Launch Android Virtual Device Manager, select <code>New...</code>
	1. Enter <code>avd-test</code> under <code>AVD Name</code>
	2. Select <code>Galaxy Nexus</code> under <code>Device</code>
	3. Select <code>Android 4.2.2 - API Level 19</code> under <code>Target</code>
	4. Select <code>ARM (armeabi-v7a)</code> under <code>CPU/ABI</code>
	5. Select <code>WXGA720</code> under <code>Skin</code>
	6. Leave as <code>None</code> for both <code>Front Camera</code> and <code>Back Camera</code>
	7. (Optional) Increase <code>VM Heap</code> to <code>256 (or higher)</code>
	8. (Optional) Check <code>Use Host GPU</code>
	9. Click <code>OK</code>
3. Select <code>avd-test</code> and click <code>Start...</code>

## Install APK file

1. Launch Android SDK Manager, and install
	1. Android SDK Tools
	2. Android SDK Platform-tools
2. Add <code>{android sdk directory}/tools</code> and <code>{android sdk directory}/platform-tools</code> to your PATH
3. Connect your device or emulator
4. Open terminal and type <code>adb install WikiDroid.apk</code>


## Source code setup

1. Launch Android SDK Manager, and install
	1. Android SDK Tools
	2. Android SDK Platform-tools
	3. Android SDK Build-tools - 19.0.2
	4. Android 4.4.2 (API 19)
		1. (Required) SDK Platform
		3. (Optional) Google APIs
		4. (Optional) Source for Android SDK
2. Depends on the IDE you are using, import the project (we use Eclipse as an example)
	1. Select <code>File > Import...</code>
	2. In the dialog, select <code>Android > Existing Android Code Into Workspace</code>, click <code>Next ></code>
	3. Click <code>Browse</code>, select <code>WikiDroid</code> folder
	4. (Optional) Check <code>Copy projects into workspace</code>
	5. Click <code>Finish</code>
3. Connect your Android device or emulator
4. From menu, select <code>Run > Run</code>

## How to use

* Open an article
	* Click the search button on the action bar
	* Type in the article name
	* Click the search button on the keyboard
* Open a new tab
	* Swipe left (or click the "drawer"" icon on the action bar) to open navigation drawer
	* Click the "plus" icon
* Switch tabs
	* Open navigation drawer
	* Click the tab you want to see
* Close tab
	* Open navigation drawer
	* Click "close" button inside the drawer
* Switch article languages
	* Open an article
	* Click "menu" button (or the physical button on the phone), click "Languages"
	* Click the desire language
	* Click "OK"
* Save article as bookmark
	* Open an article
	* Open navigation drawer
	* Click "star" icon
* Save article for offline access
	* Open an article
	* Open menu
	* Click "Save Article"
* Show all saved article and links
	* Open menu
	* Click "List of Articles Saved"