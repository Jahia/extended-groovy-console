# Extended Groovy Console   
* [Using a custom script](#how-to-use-custom) 
* [Using a predefined script](#how-to-use-predefined) 
  * [Script configuration](#configuration) 
  * [Example](#example) 


## <a name="how-to-use-custom"></a>Using a custom script

You can run a custom script here, writing or pasting it into the textarea.

If your script has to generate some output, you can use the built in logger: 

    log.info("Some output")

## <a name="how-to-use-predefined"></a>Using a predefined script

You can as well package in any of your modules a predefined script, 
which can then be conveniently run from the console without you have to copy and paste it.

Your predefined scripts have to be defined in a specific folder.

    src/main/resources/META-INF/extendedGroovyConsole
    
You can as well define your scripts in `vsrc/main/resources/META-INF/extendedGroovyConsole`.
The those scripts would be availabe in both the regular and extended groovy consoles. Be careful 
to no use any specificity of the extended console then.    

### <a name="configuration"></a>Script configuration

If some predefined script requires some configurations, then you have to create in the same 
folder a file with the same name as the script and .properties as an extension. 

    src/main/resources/META-INF/extendedGroovyConsole/myScript.groovy
    src/main/resources/META-INF/extendedGroovyConsole/myScript.properties

In this file, you can declare and configure the required parameters

* __script.title__: title of the script
* __script.description__: short description of the script
* __script.visibilityCondition__: [visibility condition](#visibility-condition) for the script
* __script.parameters.names__: comma separated list of parameters
* __script.param.xxx.type__: type of the parameter xxx
* __script.param.xxx.label__: label for the parameter xxx
* __script.param.xxx.default__: default value of the parameter xxx

**Parameter types**

__Allowed values__: checkbox, text, textarea, choicelist  \
__Default value__: checkbox

#### Parameter type: checkbox

For a checkbox parameter, the checkbox is unchecked by default, use _true_ as a default value otherwise

#### Parameter type: text

For a text parameter, the input field is empty by default, unless a default value is specified for the parameter.

#### Parameter type: textarea

For a textarea parameter, the input field is empty by default, unless a default value is specified for the parameter.

#### Parameter type: choicelist

You need to declare the values to be available in the list.

* __script.param.xxx.values__ : comma separated list of static values. A value can be just a key, or a key and label (separated with a column).
* __script.param.xxx.dynamicvalues__ : dynamic values generated by a choicelist initializer (using the same syntax as in a CND file)

If a default value is defined, then it is preselected. Static and dynamic values can be combined.

**Example:**

    script.parameters.names=workspace, site
    script.param.workspace.type=choicelist
    script.param.workspace.values=default, live
    script.param.site.type=choicelist
    script.param.site.values=all:All sites
    script.param.site.dynamicvalues=nodes=/sites;jnt:virtualsite
    
#### <a name="visibility-condition">Visibility condition

You might need your script to be available only on some environments. You can make its availability depend on
some `jahia.properties` parameter or system property.

The script will be available in the list only if the parameter `environment.continuousIntegration`
is defined and equal to `true`: 

    script.visibilityCondition=environment.continuousIntegration
    
The script will be available in the list only if the parameter `operatingMode`
is defined and equal to `development`: 

    script.visibilityCondition={operatingMode=development}    


### <a name="example"></a>Example

**helloworld.properties**

    script.title=Hello world demo
    script.description=Tell your name to the script to get a personalized hello message!
    script.parameters.names=active, name
    script.param.active.default=true
    script.param.name.type=text
    script.param.name.label=User name
        

**helloworld.groovy**

    if (active) {
        log.info(String.format("Hello %s!!!", name == null || name.trim().length() == 0 ? "world" : name))
    } else {
        log.info("On mute")
    }
        