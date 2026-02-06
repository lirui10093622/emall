package org.emall.search.builder.product;

import co.elastic.clients.elasticsearch._types.Script;

/**
 * @author Li Rui
 * @date 2025-11-12
 */
public class ProductScriptBuilder {

    public static Script isTalentPoolOperator(String talentPoolOperator) {
        return Script.of(sc -> sc.inline(i -> i
                .source(" if (doc['talentPoolOperatorEmployeeJobnumber'].size() > 0 && doc['talentPoolOperatorEmployeeJobnumber'].value.equals('" + talentPoolOperator + "')) {" +
                        "   return true " +
                        " } " +
                        " return false")
                .lang("painless")));
    }

    public static Script curStageOperatorOrder(String curStageOperator) {
        return Script.of(s -> s.inline(i -> i
                .source(" if (doc['curStageOperatorNumber'].size() > 0 && doc['curStageOperatorNumber'].value.contains('" + curStageOperator + "')) {" +
                        "   return 1; " +
                        " } " +
                        " return 2;")
                .lang("painless")
        ));
    }

    public static Script myResumeCountGroupScript(String curEmployeeJobnumber) {
        return Script.of(s -> s.inline(i -> i
                .source(" def allowedOperators = ['" + curEmployeeJobnumber + "', '系统操作', 'X000002', '招聘简历数字人', '招聘数字人', 'System']; \n" +
                        " def commonTalentPoolsIds = [1, 3, 4, 8, 9]; \n" +
                        " def flag = ''; \n" +
                        " if (doc['allocStatus'].size() > 0 && doc['allocStatus'].value == 2 && doc['talentPoolsId'].size() > 0 && doc['talentPoolsId'].value == 6 && doc['suspectedTag'].size() > 0 && doc['suspectedTag'].value == 2) { \n" +
                        "     flag = flag + 'a'; \n" +
                        " } \n" +
                        " if (doc['allocStatus'].size() > 0 && doc['allocStatus'].value == 1 && ((doc['talentPoolsId'].size() > 0 && doc['talentPoolsId'].value == 6) || doc['talentPoolOperatorEmployeeJobnumber'].size() == 0 || (doc['talentPoolOperatorEmployeeJobnumber'].size() > 0 && !allowedOperators.contains(doc['talentPoolOperatorEmployeeJobnumber'].value)))) { \n" +
                        "     flag = flag + 'b'; \n" +
                        " } \n" +
                        " if (doc['allocStatus'].size() > 0 && doc['allocStatus'].value == 3 && ((doc['talentPoolsId'].size() > 0 && doc['talentPoolsId'].value == 6) || doc['talentPoolOperatorEmployeeJobnumber'].size() == 0 || (doc['talentPoolOperatorEmployeeJobnumber'].size() > 0 && !allowedOperators.contains(doc['talentPoolOperatorEmployeeJobnumber'].value)))) { \n" +
                        "     flag = flag + 'c'; \n" +
                        " } \n" +
                        " if ((doc['talentPoolsId'].size() > 0 && doc['talentPoolsFlag'].size() > 0 && doc['mergeIntroduceNumber'].size() > 0 && commonTalentPoolsIds.contains(doc['talentPoolsId'].value) && doc['talentPoolsFlag'].value == true && doc['mergeIntroduceNumber'].value.contains('" + curEmployeeJobnumber + "')) || (doc['talentPoolOperatorEmployeeJobnumber'].size() > 0 && allowedOperators.contains(doc['talentPoolOperatorEmployeeJobnumber'].value))) { \n" +
                        "     flag = flag + 'd'; \n" +
                        " } \n" +
                        " if (doc['allocStatus'].size() > 0 && doc['suspectedTag'].size() > 0 && doc['suspectedTag'].value == 1) { \n" +
                        "     flag = flag + 'e'; \n" +
                        " } \n")
                .lang("painless")
        ));
    }
}