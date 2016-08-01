package com.tangblack.configcomparer

/**
 * Created by tangblack on 2016/8/1.
 */

/**
 * 讀設定檔，會區分設定有無開啟。
 *
 * @param path
 * @return
 */
def readConfigAndDistinguish(String path)
{
    File file = new File(path)
    List enableConfigList = new ArrayList()
    List disableConfigList = new ArrayList()
    file.eachLine { line ->
        if (line.trim().size() == 0) // 忽略空白行
        {
            return // Jump!
        }

        if (line.startsWith("#"))
        {
            disableConfigList.add(line)
        }
        else
        {
            enableConfigList.add(line)
        }
    }

    log("enableConfigList.size()=$enableConfigList.size")
    enableConfigList.each {element -> log(element)}
    log("disableConfigList.size()=$disableConfigList.size")
    disableConfigList.each {element -> log(element)}

    return [enableConfigList, disableConfigList] // 一次回傳兩個串列
}

/**
 * 讀設定檔，不會區分設定有無開啟。
 *
 * @param path
 * @return
 */
def readConfig(String path)
{
    File file = new File(path)
    List configList = new ArrayList()
    file.eachLine { line ->
        if (line.trim().size() == 0) // 忽略空白行
        {
            return // Jump!
        }

        configList.add(line)
    }

    log("configList.size()=$configList.size")
    configList.each {element -> log(element)}

    return configList
}

def compare(String inputConfig, List inputConfigList)
{
    String key
    String[] stringArray = inputConfig.split("=")
    if (stringArray.size() == 1)
    {
        key = stringArray[0].trim()
        log("inputConfig is $inputConfig, key is $key")
    }
    else if (stringArray.size() == 2)
    {
        key = stringArray[0].trim()
        String value = stringArray[1].trim()
        log("inputConfig is $inputConfig, key is $key, value is $value")
    }
    else
    {
        throw new RuntimeException("Illegal format, inputConfig=$inputConfig")
    }

    List findConfigList = new ArrayList()
    inputConfigList.each {String config ->
        if (config.indexOf(key) != -1)
        {
            findConfigList.add(config)
        }
    }

    return findConfigList
}

def log(String message)
{
    boolean debug = false; // 要印 debug 訊息要設成 true

    if (debug)
    {
        println(message)
    }
}




// 主程式開始
// 應該修改路徑就夠用了！！
String configAFilePath = "/Users/tangblack/IdeaProjects/groovy_mine/config_comparer/src/com/tangblack/configcomparer/test/config_a.txt"
String configBFilePath = "/Users/tangblack/IdeaProjects/groovy_mine/config_comparer/src/com/tangblack/configcomparer/test/config_b.txt"


// 先找出第一個檔案中所有設定，並區分是否有開啟設定
def (configAEnableConfigList, configADisableConfigList) = readConfigAndDistinguish(configAFilePath)
println("掃描 $configAFilePath")
println("找到 $configAEnableConfigList.size 個有開啟的設定")
println("找到 $configADisableConfigList.size 個沒有開啟的設定")


// 找出第二個檔案中所有設定
List configBConfigList = readConfig(configBFilePath)
println("比對 $configBFilePath")
// 第一個檔案有開啟的設定是否有出現在第二個檔案中
configAEnableConfigList.each {config ->
    List findConfigList = compare(config, configBConfigList)
    if (findConfigList.size() == 0)
    {
        println("$config -> 沒有找到")
    }
    else
    {
        println("$config -> 找到 $findConfigList.size 個 $findConfigList")
    }
}
// 第一個檔案沒有開啟的設定是否有出現在第二個檔案中
configADisableConfigList.each {config ->
    List findConfigList = compare(config, configBConfigList)
    if (findConfigList.size() == 0)
    {
        println("$config -> 沒有找到")
    }
    else
    {
        println("$config -> 找到 $findConfigList.size 個 $findConfigList")
    }
}
