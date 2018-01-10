package com.ss.ugc.android.autoandroid.parse;

import com.ss.ugc.android.autoandroid.ActionFactory;
import com.ss.ugc.android.autoandroid.CheckPointFactory;
import com.ss.ugc.android.autoandroid.TestRunner;
import com.ss.ugc.android.autoandroid.exceptions.ActionNotFoundException;
import com.ss.ugc.android.autoandroid.exceptions.InvalidConfigException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tongyao on 2018/1/4.
 */

public class SequenceParser {
    private File configFile;
    private TestRunner testRunner;

    private static class ConfigBean {
        String type;
        String name;
        String params;

        public boolean isAction() {
            return "action".equals(type);
        }

        boolean isCheckPoint() {
            return "cp".equals(type);
        }
    }

    public SequenceParser(TestRunner runner, File configFile) {
        this.configFile = configFile;
        this.testRunner = runner;
    }

    public void parse() throws InvalidConfigException, ActionNotFoundException {
        try {
            FileInputStream fileInputStream = new FileInputStream(configFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String str;
            while ((str = reader.readLine()) != null) {
                ConfigBean configBean = parseLine(str);
                if (configBean == null) {
                    continue;
                }
                if (configBean.isAction()) {
                    testRunner.addAction(ActionFactory.getActionByName(configBean.name, configBean.params));
                } else if (configBean.isCheckPoint()) {
                    testRunner.addCheckPoint(CheckPointFactory.getCheckPointByName(configBean.name));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //TODO 测试用例执行错误
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ConfigBean parseLine(String str) throws InvalidConfigException {
        if (str.startsWith("#")) {
            return null;
        }
        int typeIndex = str.indexOf(":");
        String type = null;
        String name = null;
        String params = null;
        ConfigBean configBean = null;
        if (typeIndex != -1) {
            type = str.substring(0, typeIndex);
            int nameIndex = str.indexOf(":", typeIndex + 1);
            if (nameIndex != -1 && checkTypeOrName(type)) {
                name = str.substring(typeIndex + 1, nameIndex);
                int paramIndex = str.indexOf(":", nameIndex + 1);
                if (paramIndex != -1 && checkTypeOrName(name)) {
                    params = str.substring(nameIndex + 1);
                    configBean = new ConfigBean();
                    configBean.type = type.trim();
                    configBean.name = name.trim();
                    configBean.params = params.trim();
                }
            }
        }
        return configBean;
    }

    public static boolean checkTypeOrName(String str) throws InvalidConfigException {
        // TODO
        return true;
    }
}
