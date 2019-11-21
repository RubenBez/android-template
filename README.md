# Android App Template 



## !!IMPORTANT!!

There is no scripts to rename the project. DON'T JUST DO A FIND REPLACE FOR THE WORD TEMPLATE. 

There are spesific files that need to be renamed in-order for this to work proparly.

You can rename the folder you cloned though, ie. `android-template` > `<project-name>`

### Package name

Ok, so this is the most diffucalt one since gradle and android relay heavely on them. To rename them, first rename the folders in `app/src/main/java/` to mach your package name (ie. if you package is `tech.bitcube.template` then the folder structure would be `tech/bitcube/template`). *Note: The template folder is the `root` folder where all the code is. You should create your new folder structure in the `java` folder and just move that and rename it to the `app name`*

NB: Your `app name` should be in camelCase. Try to make it as short as posible, I recomend giving your project a code name to use in development, something that is only one word and relates to your project ie. a braai app would use something like cinder or a bird app would use something like crane. 

After you renamed your folders, it is time to rename the package refrences in files, spesificaly files in the `root` folder. DON'T RENAME FILES IN OTHER DIRECTORIES YET. In the `root` folder, you can do a find-replace in all the files for `tech.bitcube.template` to `your.package.name` (This should 100% match your folder structure).

Now it is time for the small chages:

1. `AndroidManifest.xml` 

   The *package* attribute of the \<manifest\> tag 

   The *name* attribute of \<application\> (Note: Leave the .Application part, only change the package)

   The *name* attribute of \<activity\> (Note: Leave the .MainActivity part, only change the package)

2. `app/build.gradle` 

   ```gradle
   android {
   ...
     defaultConfig {
       applicationId '<your.package.name>'
       ...
     }
   }
   ```

That is it for the packages. If you open the project in Android studio it should be able to build and deploy to a device

### Application name

This is probably the easiest one of them all. In `app/src/main/res/values/strings.xml` Just change the `app_name` to your `application name`. This name is the one users will see.

### Individual files

The follwoing files can be refactored in Android Studio:

-  `data/db/TemplateDatabase.kt`
- `data/network/TemplateApiService.kt`
- `data/network/TemplateNetworkDataSource.kt`
- `data/network/TemplateNetworkDataSourceImpl.kt`
- `internal/glide/TemplateAppGlideModule.kt`



That is all. The app shold now be yours and you can changes it how ever you please.

## Gettings Started

Clone it.

```
git clone https://github.com/RubenBez/android-template.git 
```

Read the notice above if you want to rename the project to something else.



## Documentation

WIP

## Architecture

WIP

## Folder Structure

WIP
