//  正解の配列(配列の順番は適当でOK)
ans = new Array("Definition1","Creation4","Creation2_1","Component3","Component2_3","Component3_2","Component4_4","Component5_1","Component6_1","Component7_2","Component8_1","Component9_3","Constructor1","Setter2","Method4","Auto3","Auto2_2","Auto3_1","Auto4_2","Auto5_1","Auto6_2","Auto7_3","Auto8_4","Auto9_2","Auto10_3","Include2","Include2_1","Include3_2","Namespace2","Instance2","Instance2_2","Instance3_2","Instance4_2","Instance5_1","Instance6_1","Instance7_1","Life1","Life2_2","Life3_1","S2Use2","S2Use2_4","S2Servlet4","S2Servlet2_3","S2Servlet3_3","App1","Metadata3","Metadata2_1","Metadata3_1","Ognl2","Ognl2_1","Ognl3_3","Ognl4_3","Ognl5_4","Ognl6_3","Ognl7_3","Ognl8_3","Ognl9_1","Ognl10_2","Ognl11_4");
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
                    location.href="DIContainerAns.html#" + linkName
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
                    location.href="DIContainerAns.html#" + linkName
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