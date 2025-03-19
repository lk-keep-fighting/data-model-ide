package com.aims.datamodel.sdk.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

    /**
     * 将汉字转换为全拼
     *
     * @param text 要转换的汉字文本
     * @return 转换后的拼音字符串，各个拼音之间以空格分隔
     */
    public static String toPinyin(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();

        try {
            for (int i = 0; i < chars.length; i++) {
                if (Character.toString(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    // 判断是否为汉字
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        result.append(pinyinArray[0]);
                        if (i < chars.length - 1) {
                            result.append(" ");
                        }
                    }
                } else {
                    // 非汉字原样保留
                    result.append(chars[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new RuntimeException("转换拼音失败", e);
        }

        return result.toString().trim();
    }


    /**
     * 汉字转拼音(去除特殊字符)
     *
     * @param text
     * @return
     */
    public static String toPurePinyin(String text) {
        return toPinyin(text).strip().replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * 获取汉字的首字母
     *
     * @param text 要转换的汉字文本
     * @return 转换后的首字母字符串
     */
    public static String getFirstLetters(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();

        try {
            for (char aChar : chars) {
                if (Character.toString(aChar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(aChar, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        result.append(pinyinArray[0].charAt(0));
                    }
                } else {
                    result.append(aChar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw new RuntimeException("转换拼音首字母失败", e);
        }

        return result.toString().strip().replaceAll("[^a-zA-Z0-9]", "");
    }
}