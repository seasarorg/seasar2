//  正解の配列(配列の順番は適当でOK)
    ans = new Array("Mock2", "Mock2_4", "Mock3_3", "Use1", "Use2_2", "Use3_4", "Unit4", "Auto1",
                    "Methods3", "Include3", "Include2_4", "Value3", "Value2_2", "SetUp3", "SetUp2_1", "tearDown3",
                    "tearDown2_2", "tx1", "assert4", "dao3", "readXls1","readXls2_4", "reload4",
                    "reload2_4", "readXlsWriteDb2", "readXlsWriteDb2_4","readXlsWriteDb3_2",
                    "ExcelMake2","SqlReader1","XlsWriter4","DaoTestCase3","DaoTestCase2_2");
function check(linkName,linkNextName){
    count = 0;
    message = "";
    //x変数の初期化
    x = 0;
    for(i = 0; i<4; i++){
        if(document.myForm.elements["" + linkName][i].checked){
            if(answerCheck(document.myForm.elements["" + linkName][i].value)){
                message = "正解です!\n解説を表示しますか？";
                flag=confirm(message);
                if(flag){
                    location.href="testtechAns.html#" + linkName
                }
                else{
                    if(linkNextName != ""){
                        flag=confirm("次の問題に移りますか？");
                        if(flag){
                            location.href = "#" + linkNextName
                        }
                    }
                }
            }
            else{
                message = "不正解です。\n解説を表示しますか？";
                flag=confirm(message);
                if(flag){
                    location.href="testtechAns.html#" + linkName
                }
            }
            return true;
        }
        count = count + 1;
    }
    
    if(count == 4){
        alert("いずれかにチェックを入れてから「解答へGO」ボタンを押してください");  
    }
}

function answerCheck(answer){
     
    for(i = 0 ; i<ans.length; i++){
        if(ans[i] == answer){
            return true;
        }
    }
    return false;
}