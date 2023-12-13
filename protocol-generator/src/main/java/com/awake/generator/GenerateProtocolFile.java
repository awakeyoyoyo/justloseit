/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.awake.generator;


import com.awake.exception.UnknownException;
import com.awake.util.FileUtils;
import com.awake.util.base.StringUtils;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static com.awake.util.base.StringUtils.TAB;
import static com.awake.util.base.StringUtils.TAB_ASCII;


/**
 * @author godotg
 */
public abstract class GenerateProtocolFile {



    public static AtomicInteger index = new AtomicInteger();

    public static StringBuilder addTab(StringBuilder builder, int deep) {
        builder.append(TAB.repeat(Math.max(0, deep)));
        return builder;
    }

    public static StringBuilder addTabAscii(StringBuilder builder, int deep) {
        builder.append(TAB_ASCII.repeat(Math.max(0, deep)));
        return builder;
    }

    /**
     * 给每行新增若干Tab
     */
    public static String addTabs(String str, int deep) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        var splits = str.split(FileUtils.LS_REGEX);
        var builder = new StringBuilder();
        for (var split : splits) {
            builder.append(TAB.repeat(Math.max(0, deep)));
            builder.append(split).append(FileUtils.LS);
        }
        return builder.toString();
    }

    public static int indexOf(Field[] fields, Field field) {
        for (int index = 0; index < fields.length; index++) {
            if (fields[index].equals(field)) {
                return index;
            }
        }
        throw new UnknownException();
    }

}
