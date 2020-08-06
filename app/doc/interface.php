<?php
$senderuin=$_POST['senderuin'];//发送者QQ
$msgtype=$_POST['msgtype'];//消息类型
$msgseq=$_POST['msgseq'];//消息id（局部
$uniseq=$_POST['uniseq'];//消息id（全局
$msgUid=$_POST['msgUid'];//消息id（世界

$msg=$_POST['msg'];//消息内容
$selfuin=$_POST['selfuin'];//自己的QQ号
$frienduin=$_POST['frienduin'];//群号或好友QQ号
$istroop=$_POST['istroop'];//是否是群聊 1代表是 0代表不是
$time=$_POST['time'];//消息时间戳

if($msgtype==-1049){//是回复的形式，则获取回复的消息
	$baseReplayInfo_id=$_POST['baseReplayInfo_id'];
	$baseReplayInfo_msg=$_POST['baseReplayInfo_msg'];
	$baseReplayInfo_senderuin=$_POST['baseReplayInfo_senderuin'];
}
/*
  参数	 msg 		文本 待发送的内容
		 frienduin	要发送给的群号或好友QQ号
		 selfuin	自己的QQ号
		 isreply	是否是回复的形式
*/
function build_reply_msg($msg=null,$frienduin=null,$selfuin=null,$isreply=null){
	$respond=array();
	if($msg==null){
		$respond['send']=false;
	}else{
		$respond['send']=true;
		$respond['msg']=$msg;
		if($frienduin!=null)
			$respond['frienduin']=$frienduin;
		if($selfuin!=null)
			$respond['selfuin']=$selfuin;
		if($isreply!=null)
			$respond['isreplay']=$isreply;
	}
	return json_encode($respond);
}

if($senderuin=='特定的QQ号，只有此QQ号才自动回复' && $istroop==0 ){//判断发消息者是不是某人的QQ，而且是私聊
	//让机器人回复消息
	//build_reply_msg('回复测试！收到的消息内容：'.$msg,null,null,true); 这是带回复形式的
	//build_reply_msg('回复测试！收到的消息内容：'.$msg); 这是不带回复形式的
	echo build_reply_msg('回复测试！收到的消息内容：'.$msg,null,null,true);
}else{
	//不回复
	echo build_reply_msg();
}
?>