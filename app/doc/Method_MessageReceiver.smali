.class public Lakas;
.super Ljava/lang/Object;
.source "P"


# direct methods
.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;JJJ)J
    .locals 3

    .prologue
    .line 132
    .line 133
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getLongAccountUin()J

    move-result-wide v0

    .line 134
    cmp-long v2, p1, v0

    if-nez v2, :cond_0

    .line 151
    :goto_0
    return-wide p3

    .line 138
    :cond_0
    cmp-long v0, p3, v0

    if-nez v0, :cond_1

    move-wide p3, p1

    .line 140
    goto :goto_0

    .line 144
    :cond_1
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2

    .line 145
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    const-string v2, "<---decodeC2CMessagePackage: neither fromUin nor toUin is selfUin.use peerUin instead."

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->e(Ljava/lang/String;ILjava/lang/String;)V

    :cond_2
    move-wide p3, p5

    .line 147
    goto :goto_0
.end method

.method public static a([B)J
    .locals 8

    .prologue
    .line 1358
    array-length v0, p0

    const/4 v1, 0x4

    if-eq v0, v1, :cond_0

    .line 1359
    const-wide/16 v0, 0x0

    .line 1367
    :goto_0
    return-wide v0

    .line 1362
    :cond_0
    const/4 v0, 0x0

    aget-byte v0, p0, v0

    and-int/lit16 v0, v0, 0xff

    shl-int/lit8 v0, v0, 0x18

    int-to-long v0, v0

    .line 1363
    const/4 v2, 0x1

    aget-byte v2, p0, v2

    and-int/lit16 v2, v2, 0xff

    shl-int/lit8 v2, v2, 0x10

    int-to-long v2, v2

    .line 1364
    const/4 v4, 0x2

    aget-byte v4, p0, v4

    and-int/lit16 v4, v4, 0xff

    shl-int/lit8 v4, v4, 0x8

    int-to-long v4, v4

    .line 1365
    const/4 v6, 0x3

    aget-byte v6, p0, v6

    and-int/lit16 v6, v6, 0xff

    int-to-long v6, v6

    .line 1367
    add-long/2addr v0, v2

    add-long/2addr v0, v4

    add-long/2addr v0, v6

    goto :goto_0
.end method

.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    .locals 2

    .prologue
    .line 197
    .line 198
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v0

    .line 199
    if-eqz v0, :cond_0

    invoke-virtual {v0, p1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    .line 200
    const v0, 0x7f0f4308

    invoke-static {v0}, Lajya;->a(I)Ljava/lang/String;

    move-result-object v0

    .line 213
    :goto_0
    return-object v0

    .line 202
    :cond_0
    const/4 v0, 0x1

    const/4 v1, 0x0

    invoke-static {p0, p1, p2, v0, v1}, Lbbcz;->a(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;Ljava/lang/String;II)Ljava/lang/String;

    move-result-object v1

    .line 205
    if-ne v1, p1, :cond_1

    .line 206
    const/16 v0, 0x33

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lajxl;

    .line 207
    invoke-virtual {v0, p1}, Lajxl;->e(Ljava/lang/String;)Lcom/tencent/mobileqq/data/Friends;

    move-result-object v0

    .line 208
    if-eqz v0, :cond_1

    .line 209
    invoke-static {v0}, Lbbcz;->a(Lcom/tencent/mobileqq/data/Friends;)Ljava/lang/String;

    move-result-object v0

    goto :goto_0

    :cond_1
    move-object v0, v1

    goto :goto_0
.end method

.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;)Lmsf/msgsvc/msg_svc$PbSendMsgReq;
    .locals 11

    .prologue
    .line 224
    const/4 v0, 0x0

    .line 225
    const/4 v1, 0x0

    .line 226
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x3e8

    if-ne v2, v3, :cond_0

    move-object v0, p1

    .line 227
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForText;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 228
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForText;->msgVia:I

    move v8, v0

    move-object v9, v1

    .line 309
    :goto_0
    if-nez v9, :cond_1c

    .line 310
    const/4 v0, 0x0

    .line 536
    :goto_1
    return-object v0

    .line 229
    :cond_0
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x419

    if-ne v2, v3, :cond_1

    move-object v0, p1

    .line 230
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForReplyText;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForReplyText;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 231
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForReplyText;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForReplyText;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto :goto_0

    .line 232
    :cond_1
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x808

    if-ne v2, v3, :cond_2

    move-object v0, p1

    .line 233
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForText;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto :goto_0

    .line 234
    :cond_2
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7d0

    if-ne v2, v3, :cond_3

    move-object v0, p1

    .line 236
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 237
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForPic;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto :goto_0

    .line 239
    :cond_3
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7d2

    if-ne v2, v3, :cond_4

    move-object v0, p1

    .line 241
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPtt;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 242
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPtt;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto :goto_0

    .line 244
    :cond_4
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7d7

    if-ne v2, v3, :cond_5

    move-object v0, p1

    .line 246
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForMarketFace;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 247
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto :goto_0

    .line 249
    :cond_5
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x80a

    if-ne v2, v3, :cond_8

    .line 251
    instance-of v2, p1, Lcom/tencent/mobileqq/data/MessageForMarketFace;

    if-eqz v2, :cond_6

    move-object v0, p1

    .line 252
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForMarketFace;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 253
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForMarketFace;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto :goto_0

    .line 254
    :cond_6
    instance-of v2, p1, Lcom/tencent/mobileqq/data/MessageForText;

    if-eqz v2, :cond_7

    move-object v0, p1

    .line 255
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForText;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 256
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForText;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto/16 :goto_0

    .line 257
    :cond_7
    instance-of v2, p1, Lcom/tencent/mobileqq/data/MessageForPic;

    if-eqz v2, :cond_3f

    move-object v0, p1

    .line 258
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 259
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForPic;->msgVia:I

    move v8, v0

    move-object v9, v1

    goto/16 :goto_0

    .line 262
    :cond_8
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7f7

    if-ne v2, v3, :cond_9

    move-object v0, p1

    .line 263
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForApollo;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForApollo;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 265
    :cond_9
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7db

    if-eq v2, v3, :cond_a

    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x803

    if-eq v2, v3, :cond_a

    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1b5a

    if-ne v2, v3, :cond_b

    :cond_a
    move-object v0, p1

    .line 269
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForStructing;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForStructing;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 271
    :cond_b
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7da

    if-ne v2, v3, :cond_c

    move-object v0, p1

    .line 273
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForFunnyFace;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForFunnyFace;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 274
    :cond_c
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7e4

    if-ne v2, v3, :cond_d

    move-object v0, p1

    .line 275
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForShakeWindow;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForShakeWindow;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 276
    :cond_d
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x7e6

    if-ne v2, v3, :cond_e

    move-object v0, p1

    .line 277
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 278
    :cond_e
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x817

    if-ne v2, v3, :cond_f

    move-object v0, p1

    .line 279
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForLightVideo;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 280
    :cond_f
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1390

    if-ne v2, v3, :cond_10

    move-object v0, p1

    .line 281
    check-cast v0, Lcom/tencent/mobileqq/data/ChatMessage;

    const/4 v2, 0x0

    invoke-static {v0, v2}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;Z)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 282
    :cond_10
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1395

    if-ne v2, v3, :cond_11

    move-object v0, p1

    .line 283
    check-cast v0, Lcom/tencent/mobileqq/data/ChatMessage;

    invoke-static {v0}, Laxaq;->c(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 284
    :cond_11
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1396

    if-ne v2, v3, :cond_12

    move-object v0, p1

    .line 285
    check-cast v0, Lcom/tencent/mobileqq/data/ChatMessage;

    invoke-static {v0}, Laxaq;->d(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 287
    :cond_12
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1394

    if-ne v2, v3, :cond_13

    move-object v0, p1

    .line 288
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPoke;

    invoke-static {v0}, Laxaq;->e(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 289
    :cond_13
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x139a

    if-ne v2, v3, :cond_14

    move-object v0, p1

    .line 290
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPokeEmo;

    invoke-static {v0}, Laxaq;->f(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 291
    :cond_14
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x41b

    if-ne v2, v3, :cond_15

    move-object v0, p1

    .line 292
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForLongTextMsg;

    const/4 v2, 0x1

    invoke-static {v0, v2}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForLongTextMsg;Z)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 293
    :cond_15
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x41c

    if-ne v2, v3, :cond_16

    .line 294
    new-instance v0, Ltencent/im/msg/im_msg_body$RichText;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$RichText;-><init>()V

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 295
    :cond_16
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x40b

    if-ne v2, v3, :cond_17

    move-object v0, p1

    .line 296
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForMixedMsg;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForMixedMsg;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 297
    :cond_17
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x809

    if-ne v2, v3, :cond_18

    move-object v0, p1

    .line 298
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForTroopStory;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForTroopStory;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 299
    :cond_18
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1b59

    if-ne v2, v3, :cond_19

    move-object v0, p1

    .line 300
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForScribble;

    invoke-static {v0}, Laxaq;->g(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 301
    :cond_19
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1398

    if-ne v2, v3, :cond_1a

    move-object v0, p1

    .line 302
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForArkBabyqReply;

    const/4 v2, 0x0

    invoke-static {v0, v2}, Laxaq;->c(Lcom/tencent/mobileqq/data/ChatMessage;Z)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 303
    :cond_1a
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1399

    if-ne v2, v3, :cond_1b

    move-object v0, p1

    .line 304
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForArkApp;

    const/4 v2, 0x0

    invoke-static {v0, v2}, Laxaq;->b(Lcom/tencent/mobileqq/data/ChatMessage;Z)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 305
    :cond_1b
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v3, -0x1b5d

    if-ne v2, v3, :cond_3f

    move-object v0, p1

    .line 306
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForLimitChatConfirm;

    invoke-static {v0}, Laxaq;->b(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move v8, v1

    move-object v9, v0

    goto/16 :goto_0

    .line 313
    :cond_1c
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_1d

    .line 314
    const-string v0, "MsgVia"

    const/4 v1, 0x2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v3, "send message, entrance: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 318
    :cond_1d
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v1, -0x3e8

    if-ne v0, v1, :cond_1e

    move-object v0, p1

    .line 319
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    .line 320
    iget v1, v0, Lcom/tencent/mobileqq/data/MessageForText;->mPasswdRedBagFlag:I

    if-eqz v1, :cond_1e

    .line 321
    new-instance v1, Ltencent/im/msg/im_msg_body$ExtraInfo;

    invoke-direct {v1}, Ltencent/im/msg/im_msg_body$ExtraInfo;-><init>()V

    .line 322
    iget-object v2, v1, Ltencent/im/msg/im_msg_body$ExtraInfo;->uint64_uin:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v4, v0, Lcom/tencent/mobileqq/data/MessageForText;->mPasswdRedBagSender:J

    invoke-virtual {v2, v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 323
    iget-object v2, v1, Ltencent/im/msg/im_msg_body$ExtraInfo;->uint32_msg_state_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForText;->mPasswdRedBagFlag:I

    invoke-virtual {v2, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 324
    new-instance v0, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 325
    iget-object v2, v0, Ltencent/im/msg/im_msg_body$Elem;->extra_info:Ltencent/im/msg/im_msg_body$ExtraInfo;

    invoke-virtual {v2, v1}, Ltencent/im/msg/im_msg_body$ExtraInfo;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 326
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 331
    :cond_1e
    invoke-static {p1}, Lmyb;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-eqz v0, :cond_2d

    .line 332
    new-instance v0, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 333
    new-instance v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;

    invoke-direct {v1}, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;-><init>()V

    .line 334
    invoke-static {p1}, Lmyb;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Lmyc;

    move-result-object v2

    .line 335
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->uint32_flags:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v4, v2, Lmyc;->a:I

    invoke-virtual {v3, v4}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 336
    iget-object v3, v2, Lmyc;->a:Ljava/lang/String;

    invoke-static {v3}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v3

    if-nez v3, :cond_1f

    .line 337
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->str_anon_id:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v4, v2, Lmyc;->a:Ljava/lang/String;

    invoke-virtual {v4}, Ljava/lang/String;->getBytes()[B

    move-result-object v4

    invoke-static {v4}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v4

    invoke-virtual {v3, v4}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 339
    :cond_1f
    iget-object v3, v2, Lmyc;->b:Ljava/lang/String;

    invoke-static {v3}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v3

    if-nez v3, :cond_20

    .line 340
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->str_anon_nick:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v4, v2, Lmyc;->b:Ljava/lang/String;

    invoke-virtual {v4}, Ljava/lang/String;->getBytes()[B

    move-result-object v4

    invoke-static {v4}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v4

    invoke-virtual {v3, v4}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 342
    :cond_20
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->uint32_head_portrait:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v4, v2, Lmyc;->b:I

    invoke-virtual {v3, v4}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 343
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->uint32_expire_time:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v4, v2, Lmyc;->c:I

    invoke-virtual {v3, v4}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 344
    iget-object v3, v2, Lmyc;->c:Ljava/lang/String;

    invoke-static {v3}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v3

    if-nez v3, :cond_21

    .line 345
    iget-object v3, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->str_rank_color:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v2, v2, Lmyc;->c:Ljava/lang/String;

    invoke-virtual {v2}, Ljava/lang/String;->getBytes()[B

    move-result-object v2

    invoke-static {v2}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v2

    invoke-virtual {v3, v2}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 347
    :cond_21
    iget-object v2, v1, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->uint32_bubble_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget-wide v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipBubbleID:J

    long-to-int v3, v4

    invoke-virtual {v2, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 348
    iget-object v2, v0, Ltencent/im/msg/im_msg_body$Elem;->anon_group_msg:Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;

    invoke-virtual {v2, v1}, Ltencent/im/msg/im_msg_body$AnonymousGroupMsg;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 349
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 448
    :cond_22
    :goto_2
    invoke-static {p1}, Lanml;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Ltencent/im/msg/im_msg_body$Elem;

    move-result-object v0

    .line 449
    if-eqz v0, :cond_24

    .line 450
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_23

    .line 451
    const-string v1, "Q.nearby_bank"

    const/4 v2, 0x2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "clearingElem has carry, elem:"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, v0, Ltencent/im/msg/im_msg_body$Elem;->near_by_msg:Ltencent/im/msg/im_msg_body$NearByMessageType;

    iget-object v4, v4, Ltencent/im/msg/im_msg_body$NearByMessageType;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v4}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->get()I

    move-result v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v1, v2, v3}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 453
    :cond_23
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 457
    :cond_24
    const/16 v0, 0xc7

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lakvq;

    .line 458
    if-eqz v0, :cond_27

    invoke-virtual {v0}, Lakvq;->a()Z

    move-result v1

    if-eqz v1, :cond_27

    invoke-virtual {v0}, Lakvq;->a()J

    move-result-wide v2

    const-wide/16 v4, 0x1

    cmp-long v1, v2, v4

    if-nez v1, :cond_27

    .line 459
    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/4 v2, 0x1

    if-eq v1, v2, :cond_25

    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v2, 0xbb8

    if-ne v1, v2, :cond_27

    .line 462
    :cond_25
    invoke-virtual {v0}, Lakvq;->a()Lcom/tencent/mobileqq/data/Setting;

    move-result-object v0

    .line 463
    if-eqz v0, :cond_27

    .line 464
    iget-short v1, v0, Lcom/tencent/mobileqq/data/Setting;->systemHeadID:S

    .line 465
    iget-byte v0, v0, Lcom/tencent/mobileqq/data/Setting;->bHeadType:B

    .line 467
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v2

    if-eqz v2, :cond_26

    .line 468
    const-string v2, "Q.msg.MessageHandlerUtils"

    const/4 v3, 0x4

    const/4 v4, 0x2

    new-array v4, v4, [Ljava/lang/Object;

    const/4 v5, 0x0

    const/4 v6, 0x2

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    aput-object v6, v4, v5

    const/4 v5, 0x1

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, " QQ 18 aniversary activity send system head id = "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6, v1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, ", head flag = "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    aput-object v6, v4, v5

    invoke-static {v2, v3, v4}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;I[Ljava/lang/Object;)V

    .line 471
    :cond_26
    new-instance v2, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v2}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 472
    new-instance v3, Ltencent/im/msg/im_msg_body$CommonElem;

    invoke-direct {v3}, Ltencent/im/msg/im_msg_body$CommonElem;-><init>()V

    .line 473
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$CommonElem;->uint32_service_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v5, 0xd

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 474
    new-instance v4, Ltencent/im/msg/hummer/servtype/hummer_commelem$MsgElemInfo_servtype13;

    invoke-direct {v4}, Ltencent/im/msg/hummer/servtype/hummer_commelem$MsgElemInfo_servtype13;-><init>()V

    .line 475
    iget-object v5, v4, Ltencent/im/msg/hummer/servtype/hummer_commelem$MsgElemInfo_servtype13;->uint32_sys_head_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v5, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 477
    iget-object v1, v4, Ltencent/im/msg/hummer/servtype/hummer_commelem$MsgElemInfo_servtype13;->uint32_head_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 478
    iget-object v0, v3, Ltencent/im/msg/im_msg_body$CommonElem;->bytes_pb_elem:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v4}, Ltencent/im/msg/hummer/servtype/hummer_commelem$MsgElemInfo_servtype13;->toByteArray()[B

    move-result-object v1

    invoke-static {v1}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 479
    iget-object v0, v2, Ltencent/im/msg/im_msg_body$Elem;->common_elem:Ltencent/im/msg/im_msg_body$CommonElem;

    invoke-virtual {v0, v3}, Ltencent/im/msg/im_msg_body$CommonElem;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 480
    iget-object v0, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0, v2}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 485
    :cond_27
    const/16 v0, 0x33

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lajxl;

    .line 486
    iget-boolean v0, v0, Lajxl;->a:Z

    if-eqz v0, :cond_29

    .line 487
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_28

    .line 488
    const-string v0, "KplMessage"

    const/4 v1, 0x2

    const-string v2, "get kpl global flag"

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 490
    :cond_28
    const-string v0, "msg_ext_key"

    const-string v1, "Y"

    invoke-virtual {p1, v0, v1}, Lcom/tencent/mobileqq/data/MessageRecord;->saveExtInfoToExtStr(Ljava/lang/String;Ljava/lang/String;)V

    .line 491
    iget-object v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    .line 492
    iget v3, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    .line 493
    iget-wide v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->uniseq:J

    .line 494
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lcom/tencent/mobileqq/app/message/QQMessageFacade;

    move-result-object v1

    const-string v6, "extStr"

    iget-object v7, p1, Lcom/tencent/mobileqq/data/MessageRecord;->extStr:Ljava/lang/String;

    .line 495
    invoke-virtual/range {v1 .. v7}, Lcom/tencent/mobileqq/app/message/QQMessageFacade;->a(Ljava/lang/String;IJLjava/lang/String;Ljava/lang/Object;)V

    .line 497
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lcom/tencent/mobileqq/app/message/QQMessageFacade;

    move-result-object v1

    const-string v6, "extLong"

    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->extLong:I

    .line 499
    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v7

    .line 498
    invoke-virtual/range {v1 .. v7}, Lcom/tencent/mobileqq/app/message/QQMessageFacade;->a(Ljava/lang/String;IJLjava/lang/String;Ljava/lang/Object;)V

    .line 502
    :cond_29
    invoke-static {p0, p1, v9, v8}, Laxaq;->a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Ltencent/im/msg/im_msg_body$RichText;I)Lmsf/msgsvc/msg_svc$PbSendMsgReq;

    move-result-object v1

    .line 504
    if-eqz v1, :cond_2a

    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForStructing;

    if-eqz v0, :cond_2a

    move-object v0, p1

    .line 505
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForStructing;

    .line 506
    iget-object v2, v0, Lcom/tencent/mobileqq/data/MessageForStructing;->structingMsg:Lcom/tencent/mobileqq/structmsg/AbsStructMsg;

    if-eqz v2, :cond_2a

    .line 507
    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForStructing;->structingMsg:Lcom/tencent/mobileqq/structmsg/AbsStructMsg;

    invoke-static {v0}, Lsif;->a(Lcom/tencent/mobileqq/structmsg/AbsStructMsg;)Z

    move-result v0

    if-eqz v0, :cond_2a

    .line 510
    new-instance v0, Lmsf/msgsvc/msg_ctrl$MsgCtrl;

    invoke-direct {v0}, Lmsf/msgsvc/msg_ctrl$MsgCtrl;-><init>()V

    .line 511
    iget-object v2, v0, Lmsf/msgsvc/msg_ctrl$MsgCtrl;->msg_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v3, 0x4

    invoke-virtual {v2, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 512
    iget-object v2, v1, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_ctrl:Lmsf/msgsvc/msg_ctrl$MsgCtrl;

    invoke-virtual {v2, v0}, Lmsf/msgsvc/msg_ctrl$MsgCtrl;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 517
    :cond_2a
    invoke-static {p0, p1, v1}, Lakas;->a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Lmsf/msgsvc/msg_svc$PbSendMsgReq;)Ltencent/im/msg/im_msg_body$GeneralFlags;

    move-result-object v2

    .line 518
    if-eqz v2, :cond_2b

    if-eqz v1, :cond_2b

    .line 519
    iget-object v0, v1, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$RichText;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$RichText;

    .line 520
    new-instance v3, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v3}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 521
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$Elem;->general_flags:Ltencent/im/msg/im_msg_body$GeneralFlags;

    invoke-virtual {v4, v2}, Ltencent/im/msg/im_msg_body$GeneralFlags;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 522
    iget-object v0, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->get()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v3}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    .line 526
    :cond_2b
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPic;

    if-eqz v0, :cond_2c

    .line 527
    check-cast p1, Lcom/tencent/mobileqq/data/MessageForPic;

    .line 528
    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageForPic;->msgCtrl:Lmsf/msgsvc/msg_ctrl$MsgCtrl;

    if-eqz v0, :cond_2c

    if-eqz v1, :cond_2c

    .line 529
    iget-object v0, v1, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_ctrl:Lmsf/msgsvc/msg_ctrl$MsgCtrl;

    iget-object v2, p1, Lcom/tencent/mobileqq/data/MessageForPic;->msgCtrl:Lmsf/msgsvc/msg_ctrl$MsgCtrl;

    invoke-virtual {v0, v2}, Lmsf/msgsvc/msg_ctrl$MsgCtrl;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 530
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2c

    .line 531
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v2, 0x2

    const-string/jumbo v3, "set msg_crl for MessageForPic"

    invoke-static {v0, v2, v3}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    :cond_2c
    move-object v0, v1

    .line 536
    goto/16 :goto_1

    .line 350
    :cond_2d
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/4 v1, 0x1

    if-eq v0, v1, :cond_2e

    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v1, 0x402

    if-ne v0, v1, :cond_3b

    .line 352
    :cond_2e
    const/16 v0, 0x33

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lajxl;

    .line 353
    const/16 v1, 0x3c

    invoke-virtual {p0, v1}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v1

    check-cast v1, Lcom/tencent/mobileqq/app/HotChatManager;

    .line 355
    const-string v2, "ext_key_group_chat_confess_info"

    invoke-virtual {p1, v2}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-nez v2, :cond_30

    .line 356
    invoke-static {p1}, Lamnr;->b(Lcom/tencent/mobileqq/data/MessageRecord;)Ltencent/im/msg/im_msg_body$Elem;

    move-result-object v2

    .line 357
    if-eqz v2, :cond_2f

    .line 358
    iget-object v3, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v3, v2}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 360
    :cond_2f
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v3

    if-eqz v3, :cond_30

    .line 361
    const-string v3, "Q.confess"

    const/4 v4, 0x2

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "bindGroupFirstMsgConfessInfoElem elem:"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v3, v4, v2}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 365
    :cond_30
    iget-object v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/app/HotChatManager;->a(Ljava/lang/String;)Lcom/tencent/mobileqq/data/HotChatInfo;

    move-result-object v2

    .line 366
    const/16 v1, 0x34

    invoke-virtual {p0, v1}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v1

    check-cast v1, Lcom/tencent/mobileqq/app/TroopManager;

    .line 367
    if-nez v2, :cond_31

    iget-object v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/app/TroopManager;->f(Ljava/lang/String;)Z

    move-result v1

    if-eqz v1, :cond_22

    .line 368
    :cond_31
    new-instance v3, Ltencent/im/msg/im_msg_body$PubGroup;

    invoke-direct {v3}, Ltencent/im/msg/im_msg_body$PubGroup;-><init>()V

    .line 374
    :try_start_0
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v1

    const-string/jumbo v2, "self_nick"

    const-string v4, ""

    invoke-static {v4}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v4

    invoke-static {v1, v2, v4}, Latbi;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/String;

    .line 375
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v2

    const-string/jumbo v4, "self_gender"

    const/4 v5, -0x1

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-static {v2, v4, v5}, Latbi;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v4

    .line 376
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v2

    const-string/jumbo v5, "self_age"

    const/4 v6, -0x1

    invoke-static {v6}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-static {v2, v5, v6}, Latbi;->a(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Integer;

    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v2

    .line 377
    if-ltz v2, :cond_32

    if-gez v4, :cond_39

    .line 378
    :cond_32
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lajxl;->e(Ljava/lang/String;)Lcom/tencent/mobileqq/data/Friends;

    move-result-object v2

    .line 379
    if-eqz v2, :cond_37

    .line 380
    iget v0, v2, Lcom/tencent/mobileqq/data/Friends;->age:I

    .line 381
    iget-byte v1, v2, Lcom/tencent/mobileqq/data/Friends;->gender:B

    const/4 v4, 0x2

    if-ne v1, v4, :cond_35

    const/4 v1, 0x1

    .line 382
    :goto_3
    iget-object v2, v2, Lcom/tencent/mobileqq/data/Friends;->name:Ljava/lang/String;

    .line 398
    :goto_4
    if-eqz v2, :cond_33

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v4

    if-lez v4, :cond_33

    .line 399
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$PubGroup;->bytes_nickname:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v2}, Ljava/lang/String;->getBytes()[B

    move-result-object v2

    invoke-static {v2}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v2

    invoke-virtual {v4, v2}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 401
    :cond_33
    iget-object v2, v3, Ltencent/im/msg/im_msg_body$PubGroup;->uint32_age:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v2, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 402
    iget-object v0, v3, Ltencent/im/msg/im_msg_body$PubGroup;->uint32_gender:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 408
    :goto_5
    new-instance v0, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 409
    iget-object v1, v0, Ltencent/im/msg/im_msg_body$Elem;->pub_group:Ltencent/im/msg/im_msg_body$PubGroup;

    invoke-virtual {v1, v3}, Ltencent/im/msg/im_msg_body$PubGroup;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 411
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 414
    invoke-static {p0, p1}, Lcom/tencent/mobileqq/app/HotChatManager;->a(Lcom/tencent/common/app/AppInterface;Lcom/tencent/mobileqq/data/MessageRecord;)Ltencent/im/msg/im_msg_body$Elem;

    move-result-object v0

    .line 415
    if-eqz v0, :cond_22

    .line 416
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_34

    .line 417
    const-string v1, "Q.hotchat"

    const/4 v2, 0x2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "carry authElem:"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v4, v0, Ltencent/im/msg/im_msg_body$Elem;->near_by_msg:Ltencent/im/msg/im_msg_body$NearByMessageType;

    iget-object v4, v4, Ltencent/im/msg/im_msg_body$NearByMessageType;->uint32_identify_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v4}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->get()I

    move-result v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v1, v2, v3}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 419
    :cond_34
    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v2, -0x7db

    if-ne v1, v2, :cond_3a

    .line 421
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->get()Ljava/util/List;

    move-result-object v1

    .line 422
    const/4 v2, 0x0

    invoke-interface {v1, v2, v0}, Ljava/util/List;->add(ILjava/lang/Object;)V

    .line 423
    iget-object v0, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->set(Ljava/util/List;)V

    goto/16 :goto_2

    .line 381
    :cond_35
    :try_start_1
    iget-byte v1, v2, Lcom/tencent/mobileqq/data/Friends;->gender:B

    const/4 v4, 0x1

    if-ne v1, v4, :cond_36

    const/4 v1, 0x0

    goto :goto_3

    :cond_36
    const/4 v1, 0x2

    goto :goto_3

    .line 384
    :cond_37
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getCurrentAccountUin()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Lajxl;->b(Ljava/lang/String;)Lcom/tencent/mobileqq/data/Card;

    move-result-object v2

    .line 385
    if-eqz v2, :cond_38

    .line 386
    iget-byte v0, v2, Lcom/tencent/mobileqq/data/Card;->age:B

    .line 387
    iget-short v1, v2, Lcom/tencent/mobileqq/data/Card;->shGender:S

    .line 388
    iget-object v2, v2, Lcom/tencent/mobileqq/data/Card;->strNick:Ljava/lang/String;
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    goto/16 :goto_4

    .line 390
    :cond_38
    const/4 v0, 0x0

    .line 391
    const/4 v2, 0x2

    move v10, v2

    move-object v2, v1

    move v1, v10

    goto/16 :goto_4

    .line 396
    :cond_39
    add-int/lit8 v0, v4, -0x1

    move v10, v2

    move-object v2, v1

    move v1, v0

    move v0, v10

    goto/16 :goto_4

    .line 404
    :catch_0
    move-exception v0

    .line 405
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    goto/16 :goto_5

    .line 425
    :cond_3a
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    goto/16 :goto_2

    .line 430
    :cond_3b
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v1, 0x409

    if-eq v0, v1, :cond_3c

    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v1, 0x40a

    if-ne v0, v1, :cond_3d

    .line 431
    :cond_3c
    new-instance v0, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 432
    invoke-static {v0, p1}, Lamnr;->a(Ltencent/im/msg/im_msg_body$Elem;Lcom/tencent/mobileqq/data/MessageRecord;)V

    .line 433
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 434
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_22

    .line 435
    const-string v0, "Q.confess"

    const/4 v1, 0x2

    const-string v2, "bindConfessInfoElem"

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    goto/16 :goto_2

    .line 437
    :cond_3d
    const-string v0, "ext_key_frd_chat_confess_info"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_22

    .line 438
    invoke-static {p1}, Lamnr;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Ltencent/im/msg/im_msg_body$Elem;

    move-result-object v0

    .line 439
    if-eqz v0, :cond_3e

    .line 440
    iget-object v1, v9, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 442
    :cond_3e
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_22

    .line 443
    const-string v1, "Q.confess"

    const/4 v2, 0x2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "bindC2CFirstMsgConfessInfoElem elem:"

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v2, v0}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    goto/16 :goto_2

    :cond_3f
    move v8, v1

    move-object v9, v0

    goto/16 :goto_0
.end method

.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Ljava/util/ArrayList;Laaod;)Lmsf/msgsvc/msg_svc$PbSendMsgReq;
    .locals 10
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/tencent/mobileqq/app/QQAppInterface;",
            "Lcom/tencent/mobileqq/data/MessageRecord;",
            "Ljava/util/ArrayList",
            "<",
            "Ljava/lang/String;",
            ">;",
            "Laaod;",
            ")",
            "Lmsf/msgsvc/msg_svc$PbSendMsgReq;"
        }
    .end annotation

    .prologue
    .line 983
    .line 984
    const/4 v1, 0x0

    .line 985
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v2, -0x3e8

    if-ne v0, v2, :cond_2

    move-object v0, p1

    .line 986
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/MessageForText;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v1

    move-object v0, p1

    .line 987
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForText;

    iget v0, v0, Lcom/tencent/mobileqq/data/MessageForText;->msgVia:I

    move-object v2, v1

    move v1, v0

    .line 999
    :goto_0
    if-eqz v2, :cond_0

    iget-object v0, v2, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    if-eqz v0, :cond_0

    if-eqz p2, :cond_0

    invoke-virtual {p2}, Ljava/util/ArrayList;->size()I

    move-result v0

    if-nez v0, :cond_a

    .line 1001
    :cond_0
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_1

    .line 1002
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v3, 0x2

    const-string v4, "getPbSendReqFromBlessMsg, error: %s, %s, %s, %s"

    const/4 v0, 0x4

    new-array v5, v0, [Ljava/lang/Object;

    const/4 v6, 0x0

    if-nez v2, :cond_6

    const/4 v0, 0x1

    :goto_1
    invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    aput-object v0, v5, v6

    const/4 v6, 0x1

    if-eqz v2, :cond_7

    iget-object v0, v2, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    if-nez v0, :cond_7

    const/4 v0, 0x1

    .line 1003
    :goto_2
    invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    aput-object v0, v5, v6

    const/4 v2, 0x2

    if-nez p2, :cond_8

    const/4 v0, 0x1

    :goto_3
    invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    aput-object v0, v5, v2

    const/4 v2, 0x3

    if-eqz p2, :cond_9

    invoke-virtual {p2}, Ljava/util/ArrayList;->size()I

    move-result v0

    if-nez v0, :cond_9

    const/4 v0, 0x1

    :goto_4
    invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v0

    aput-object v0, v5, v2

    .line 1002
    invoke-static {v4, v5}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v3, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1005
    :cond_1
    const/4 v0, 0x0

    .line 1143
    :goto_5
    return-object v0

    .line 988
    :cond_2
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v2, -0x7e6

    if-ne v0, v2, :cond_3

    move-object v0, p1

    .line 989
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move-object v2, v0

    goto :goto_0

    .line 990
    :cond_3
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v2, -0x7d0

    if-ne v0, v2, :cond_4

    move-object v0, p1

    .line 991
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    invoke-static {v0}, Laxaq;->a(Lcom/tencent/mobileqq/data/ChatMessage;)Ltencent/im/msg/im_msg_body$RichText;

    move-result-object v0

    move-object v2, v0

    goto :goto_0

    .line 993
    :cond_4
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_5

    .line 994
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "getPbSendReqFromBlessMsg, error msgtype, "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    iget v3, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 996
    :cond_5
    const/4 v0, 0x0

    goto :goto_5

    .line 1002
    :cond_6
    const/4 v0, 0x0

    goto :goto_1

    :cond_7
    const/4 v0, 0x0

    goto :goto_2

    .line 1003
    :cond_8
    const/4 v0, 0x0

    goto :goto_3

    :cond_9
    const/4 v0, 0x0

    goto :goto_4

    .line 1009
    :cond_a
    new-instance v0, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v0}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 1010
    new-instance v3, Ltencent/im/msg/im_msg_body$BlessingMessage;

    invoke-direct {v3}, Ltencent/im/msg/im_msg_body$BlessingMessage;-><init>()V

    .line 1011
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$BlessingMessage;->uint32_msg_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v5, 0x1

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1012
    iget-boolean v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->needUpdateMsgTag:Z

    if-eqz v4, :cond_b

    .line 1013
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$BlessingMessage;->uint32_ex_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v5, 0x1

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1017
    :goto_6
    iget-object v4, v0, Ltencent/im/msg/im_msg_body$Elem;->bless_msg:Ltencent/im/msg/im_msg_body$BlessingMessage;

    invoke-virtual {v4, v3}, Ltencent/im/msg/im_msg_body$BlessingMessage;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1018
    iget-object v3, v2, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v3, v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1020
    new-instance v4, Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-direct {v4}, Ltencent/im/msg/im_msg_body$MsgBody;-><init>()V

    .line 1021
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0, v2}, Ltencent/im/msg/im_msg_body$RichText;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1024
    new-instance v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;

    invoke-direct {v2}, Lmsf/msgsvc/msg_svc$MultiMsgAssist;-><init>()V

    .line 1025
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->msg_use:Lcom/tencent/mobileqq/pb/PBEnumField;

    const/4 v3, 0x1

    invoke-virtual {v0, v3}, Lcom/tencent/mobileqq/pb/PBEnumField;->set(I)V

    .line 1026
    invoke-virtual {p2}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v3

    :goto_7
    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_c

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    .line 1027
    new-instance v5, Lmsf/msgsvc/msg_svc$RoutingHead;

    invoke-direct {v5}, Lmsf/msgsvc/msg_svc$RoutingHead;-><init>()V

    .line 1028
    new-instance v6, Lmsf/msgsvc/msg_svc$C2C;

    invoke-direct {v6}, Lmsf/msgsvc/msg_svc$C2C;-><init>()V

    .line 1029
    iget-object v7, v6, Lmsf/msgsvc/msg_svc$C2C;->to_uin:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    invoke-static {v0}, Ljava/lang/Long;->valueOf(Ljava/lang/String;)Ljava/lang/Long;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Long;->longValue()J

    move-result-wide v8

    invoke-virtual {v7, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 1030
    iget-object v0, v5, Lmsf/msgsvc/msg_svc$RoutingHead;->c2c:Lmsf/msgsvc/msg_svc$C2C;

    invoke-virtual {v0, v6}, Lmsf/msgsvc/msg_svc$C2C;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1032
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->repeated_routing:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->add(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    goto :goto_7

    .line 1015
    :cond_b
    iget-object v4, v3, Ltencent/im/msg/im_msg_body$BlessingMessage;->uint32_ex_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v5, 0x0

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    goto :goto_6

    .line 1036
    :cond_c
    if-eqz p3, :cond_10

    .line 1037
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_d

    .line 1038
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v3, 0x2

    const-string v5, "getPbSendReqFromBlessMsg, hongbao param, temp_id: %d, video_len: %d, redbag_id: %s, redbag_amount: %d, has_readbag: %d, has_video: %d"

    const/4 v6, 0x6

    new-array v6, v6, [Ljava/lang/Object;

    const/4 v7, 0x0

    iget-wide v8, p3, Laaod;->a:J

    .line 1039
    invoke-static {v8, v9}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v8

    aput-object v8, v6, v7

    const/4 v7, 0x1

    iget-wide v8, p3, Laaod;->b:J

    invoke-static {v8, v9}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v8

    aput-object v8, v6, v7

    const/4 v7, 0x2

    iget-object v8, p3, Laaod;->a:[B

    aput-object v8, v6, v7

    const/4 v7, 0x3

    iget-wide v8, p3, Laaod;->c:J

    invoke-static {v8, v9}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v8

    aput-object v8, v6, v7

    const/4 v7, 0x4

    iget v8, p3, Laaod;->a:I

    .line 1040
    invoke-static {v8}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v8

    aput-object v8, v6, v7

    const/4 v7, 0x5

    iget v8, p3, Laaod;->b:I

    invoke-static {v8}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v8

    aput-object v8, v6, v7

    .line 1038
    invoke-static {v5, v6}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v0, v3, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1043
    :cond_d
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->uint64_temp_id:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v6, p3, Laaod;->a:J

    invoke-virtual {v0, v6, v7}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 1044
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->uint64_vedio_len:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v6, p3, Laaod;->b:J

    invoke-virtual {v0, v6, v7}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 1045
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->uint32_has_readbag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v3, p3, Laaod;->a:I

    invoke-virtual {v0, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1046
    iget v0, p3, Laaod;->a:I

    const/4 v3, 0x1

    if-ne v0, v3, :cond_f

    .line 1047
    iget-object v0, p3, Laaod;->a:[B

    if-eqz v0, :cond_e

    .line 1048
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->bytes_redbag_id:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v3, p3, Laaod;->a:[B

    invoke-static {v3}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v3

    invoke-virtual {v0, v3}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 1050
    :cond_e
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->uint64_redbag_amount:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v6, p3, Laaod;->c:J

    invoke-virtual {v0, v6, v7}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 1052
    :cond_f
    iget-object v0, v2, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->uint32_has_vedio:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v3, p3, Laaod;->b:I

    invoke-virtual {v0, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1055
    :cond_10
    iget-wide v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgUid:J

    invoke-static {v6, v7}, Laxau;->a(J)I

    move-result v0

    .line 1056
    const v3, 0xffff

    iget-wide v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgseq:J

    long-to-int v5, v6

    int-to-short v5, v5

    and-int/2addr v5, v3

    .line 1057
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lawzy;

    move-result-object v3

    invoke-virtual {v3}, Lawzy;->a()[B

    move-result-object v6

    .line 1059
    new-instance v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;

    invoke-direct {v3}, Lmsf/msgsvc/msg_svc$PbSendMsgReq;-><init>()V

    .line 1060
    iget-object v7, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v7, v4}, Ltencent/im/msg/im_msg_body$MsgBody;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1061
    iget-object v4, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_seq:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1062
    iget-object v4, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_rand:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v4, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1063
    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_via:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1064
    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->multi_msg_assist:Lmsf/msgsvc/msg_svc$MultiMsgAssist;

    invoke-virtual {v0, v2}, Lmsf/msgsvc/msg_svc$MultiMsgAssist;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1065
    if-eqz v6, :cond_11

    .line 1067
    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->sync_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-static {v6}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 1076
    :cond_11
    if-eqz v3, :cond_18

    .line 1077
    invoke-virtual {v3}, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->toByteArray()[B

    move-result-object v0

    array-length v5, v0

    .line 1078
    const/16 v0, 0xd

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a(I)Lajtb;

    move-result-object v0

    check-cast v0, Lakfv;

    .line 1080
    invoke-virtual {v0}, Lakfv;->d()I

    move-result v6

    .line 1081
    const/16 v1, 0x2c

    invoke-virtual {p0, v1}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v1

    check-cast v1, Lcom/tencent/mobileqq/bubble/BubbleManager;

    .line 1082
    invoke-virtual {v0}, Lakfv;->b()I

    move-result v2

    .line 1083
    const/4 v0, 0x0

    .line 1084
    if-lez v2, :cond_1c

    .line 1085
    const/4 v0, 0x0

    invoke-virtual {v1, v2, v0}, Lcom/tencent/mobileqq/bubble/BubbleManager;->a(IZ)Lambd;

    move-result-object v0

    .line 1086
    if-eqz v0, :cond_12

    if-eqz v0, :cond_19

    iget-object v1, v0, Lambd;->a:Ljava/util/HashMap;

    if-eqz v1, :cond_19

    iget-object v0, v0, Lambd;->a:Ljava/util/HashMap;

    .line 1087
    invoke-virtual {v0}, Ljava/util/HashMap;->size()I

    move-result v0

    if-lez v0, :cond_19

    :cond_12
    const/4 v0, 0x1

    :goto_8
    move v1, v0

    .line 1089
    :goto_9
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_13

    .line 1090
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v4, 0x2

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "bubbleId->"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v7, v2}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v2

    const-string v7, " needSend->"

    invoke-virtual {v2, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2, v1}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v0, v4, v2}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1094
    :cond_13
    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->has()Z

    move-result v0

    if-eqz v0, :cond_18

    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$RichText;->has()Z

    move-result v0

    if-eqz v0, :cond_18

    .line 1095
    iget-object v0, v3, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$RichText;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$RichText;

    .line 1096
    iget-object v2, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    if-eqz v2, :cond_18

    iget-object v2, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v2}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->has()Z

    move-result v2

    if-eqz v2, :cond_18

    .line 1097
    const/4 v2, 0x0

    .line 1098
    const/4 v4, 0x0

    .line 1099
    new-instance v7, Ltencent/im/msg/im_msg_body$GeneralFlags;

    invoke-direct {v7}, Ltencent/im/msg/im_msg_body$GeneralFlags;-><init>()V

    .line 1100
    new-instance v8, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;

    invoke-direct {v8}, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;-><init>()V

    .line 1102
    if-eqz v1, :cond_1b

    if-lez v6, :cond_14

    add-int/lit8 v1, v5, 0x7

    if-ge v1, v6, :cond_1b

    if-lez v6, :cond_1b

    :cond_14
    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipBubbleDiyTextId:I

    if-lez v1, :cond_1b

    .line 1103
    const/4 v1, 0x1

    .line 1104
    iget-object v2, v7, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint32_bubble_diy_text_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipBubbleDiyTextId:I

    invoke-virtual {v2, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1105
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v2

    if-eqz v2, :cond_15

    .line 1106
    const-string v2, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x2

    const-string v6, "add BubbleDiyTextId!"

    invoke-static {v2, v5, v6}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1111
    :cond_15
    :goto_a
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    if-eqz v2, :cond_16

    .line 1112
    const/4 v1, 0x1

    .line 1113
    iget-object v2, v7, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint32_bubble_sub_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    invoke-virtual {v2, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1114
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v2

    if-eqz v2, :cond_16

    .line 1115
    const-string v2, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x2

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "getPbSendReqFromBlessMsg, sub bubbleid: "

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    iget v9, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    invoke-virtual {v6, v9}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v2, v5, v6}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 1119
    :cond_16
    instance-of v2, p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    if-eqz v2, :cond_1a

    .line 1120
    check-cast p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    .line 1121
    iget v2, p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;->redBagType:I

    sget v5, Lcom/tencent/mobileqq/activity/photo/LocalMediaInfo;->REDBAG_TYPE_GET:I

    if-ne v2, v5, :cond_1a

    .line 1122
    const/4 v2, 0x1

    .line 1123
    iget-object v4, v8, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_red_envelope_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;->redBagType:I

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 1124
    iget-object v4, v8, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_shortVideoId:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v5, p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;->shortVideoId:Ljava/lang/String;

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v4, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 1125
    const-string v4, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x1

    const-string v6, "VideoRedbag, generalflag take redbag flag in bless"

    invoke-static {v4, v5, v6}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1130
    :goto_b
    if-eqz v2, :cond_17

    .line 1131
    const/4 v1, 0x1

    .line 1132
    iget-object v2, v7, Ltencent/im/msg/im_msg_body$GeneralFlags;->bytes_pb_reserve:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v8}, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->toByteArray()[B

    move-result-object v4

    invoke-static {v4}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v4

    invoke-virtual {v2, v4}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 1134
    :cond_17
    if-eqz v1, :cond_18

    .line 1135
    new-instance v1, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v1}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 1136
    iget-object v2, v1, Ltencent/im/msg/im_msg_body$Elem;->general_flags:Ltencent/im/msg/im_msg_body$GeneralFlags;

    invoke-virtual {v2, v7}, Ltencent/im/msg/im_msg_body$GeneralFlags;->set(Lcom/tencent/mobileqq/pb/MessageMicro;)V

    .line 1137
    iget-object v0, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->get()Ljava/util/List;

    move-result-object v0

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    :cond_18
    move-object v0, v3

    .line 1143
    goto/16 :goto_5

    .line 1087
    :cond_19
    const/4 v0, 0x0

    goto/16 :goto_8

    :cond_1a
    move v2, v4

    goto :goto_b

    :cond_1b
    move v1, v2

    goto/16 :goto_a

    :cond_1c
    move v1, v0

    goto/16 :goto_9
.end method

.method private static a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Lmsf/msgsvc/msg_svc$PbSendMsgReq;)Ltencent/im/msg/im_msg_body$GeneralFlags;
    .locals 12

    .prologue
    .line 548
    const/4 v3, 0x0

    .line 549
    const/4 v0, 0x0

    .line 550
    new-instance v4, Ltencent/im/msg/im_msg_body$GeneralFlags;

    invoke-direct {v4}, Ltencent/im/msg/im_msg_body$GeneralFlags;-><init>()V

    .line 551
    new-instance v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;

    invoke-direct {v6}, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;-><init>()V

    .line 553
    sget-object v1, Laxaf;->c:Ljava/lang/String;

    invoke-virtual {p1, v1}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 554
    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v2

    if-nez v2, :cond_3e

    .line 556
    const/4 v0, 0x1

    .line 557
    :try_start_0
    invoke-static {v1}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v2

    .line 558
    iget-object v5, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_holiday_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v5, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    move v2, v0

    .line 570
    :goto_0
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v1, -0x41c

    if-ne v0, v1, :cond_c

    .line 571
    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    if-eqz v0, :cond_b

    .line 573
    :try_start_1
    const-string v0, "babyq_set_avator"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_2

    .line 574
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 575
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xc9

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 576
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    .line 971
    :cond_0
    :goto_1
    return-object v4

    .line 560
    :catch_0
    move-exception v0

    .line 561
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_1

    .line 562
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v2, 0x2

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "getGeneralFlagElem, holiday_activie_flag.error: "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v2, v1}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 564
    :cond_1
    const/4 v0, 0x0

    move v2, v0

    goto :goto_0

    .line 577
    :cond_2
    :try_start_2
    const-string v0, "babyq_add_friend"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_4

    .line 578
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 579
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xca

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 580
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_1

    .line 614
    :catch_1
    move-exception v0

    .line 615
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_3

    .line 616
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v2, 0x2

    const-string/jumbo v3, "set guideMsgCookie error!"

    invoke-static {v1, v2, v3, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;Ljava/lang/Throwable;)V

    .line 618
    :cond_3
    const/4 v4, 0x0

    goto :goto_1

    .line 581
    :cond_4
    :try_start_3
    const-string v0, "babyq_add_troop"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    .line 582
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 583
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xcb

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 584
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto :goto_1

    .line 585
    :cond_5
    const-string v0, "babyq_game_strategy"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_6

    .line 586
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 587
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xcc

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 588
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_user_click:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v2, 0x1

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 589
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto/16 :goto_1

    .line 590
    :cond_6
    const-string v0, "babyq_game_gift"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_7

    .line 591
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 592
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xcd

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 593
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_user_click:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v2, 0x1

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 594
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto/16 :goto_1

    .line 595
    :cond_7
    const-string v0, "babyq_game_tribe"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_8

    .line 596
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 597
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xce

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 598
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_user_click:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v2, 0x1

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 599
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto/16 :goto_1

    .line 600
    :cond_8
    const-string v0, "baqyq_mayknow_people"

    iget-object v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_9

    .line 601
    new-instance v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;

    invoke-direct {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;-><init>()V

    .line 602
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/16 v2, 0xcf

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 603
    iget-object v1, v0, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->uint32_user_click:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v2, 0x1

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 604
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/babyq/babyq_cookie$BabyQCookie;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto/16 :goto_1

    .line 606
    :cond_9
    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-static {v0}, Lbbbr;->a(Ljava/lang/String;)[B

    move-result-object v0

    .line 607
    if-eqz v0, :cond_a

    .line 608
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->babyq_guide_msg_cookie:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v2

    invoke-virtual {v1, v2}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 610
    :cond_a
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_0

    .line 611
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v2, 0x2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "getGeneralFlagElem, guideMsgCookie: "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget-object v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v5, " ,bytes:"

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-static {v0}, Ljava/util/Arrays;->toString([B)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v2, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V
    :try_end_3
    .catch Ljava/lang/Exception; {:try_start_3 .. :try_end_3} :catch_1

    goto/16 :goto_1

    .line 622
    :cond_b
    const/4 v4, 0x0

    goto/16 :goto_1

    .line 629
    :cond_c
    const/16 v0, 0x2c

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/bubble/BubbleManager;

    .line 630
    if-eqz p2, :cond_13

    .line 631
    invoke-virtual {p2}, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->toByteArray()[B

    move-result-object v1

    array-length v5, v1

    .line 632
    const/16 v1, 0xd

    invoke-virtual {p0, v1}, Lcom/tencent/mobileqq/app/QQAppInterface;->a(I)Lajtb;

    move-result-object v1

    check-cast v1, Lakfv;

    .line 634
    invoke-virtual {v1}, Lakfv;->d()I

    move-result v7

    .line 636
    invoke-virtual {v1}, Lakfv;->b()I

    move-result v8

    .line 637
    const/4 v1, 0x0

    .line 638
    if-lez v8, :cond_3d

    .line 639
    const/4 v1, 0x0

    invoke-virtual {v0, v8, v1}, Lcom/tencent/mobileqq/bubble/BubbleManager;->a(IZ)Lambd;

    move-result-object v0

    .line 640
    if-eqz v0, :cond_d

    if-eqz v0, :cond_23

    iget-object v1, v0, Lambd;->a:Ljava/util/HashMap;

    if-eqz v1, :cond_23

    iget-object v0, v0, Lambd;->a:Ljava/util/HashMap;

    .line 641
    invoke-virtual {v0}, Ljava/util/HashMap;->size()I

    move-result v0

    if-lez v0, :cond_23

    :cond_d
    const/4 v0, 0x1

    .line 643
    :goto_2
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_e

    .line 644
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v9, 0x2

    new-instance v10, Ljava/lang/StringBuilder;

    invoke-direct {v10}, Ljava/lang/StringBuilder;-><init>()V

    const-string v11, "bubbleId->"

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v10, " needSend->"

    invoke-virtual {v8, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-static {v1, v9, v8}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 646
    :cond_e
    if-eqz v0, :cond_3c

    if-lez v7, :cond_f

    add-int/lit8 v0, v5, 0x7

    if-ge v0, v7, :cond_3c

    if-lez v7, :cond_3c

    :cond_f
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipBubbleDiyTextId:I

    if-lez v0, :cond_3c

    .line 647
    iget-object v0, p2, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->has()Z

    move-result v0

    if-eqz v0, :cond_3c

    iget-object v0, p2, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$RichText;->has()Z

    move-result v0

    if-eqz v0, :cond_3c

    .line 648
    iget-object v0, p2, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$RichText;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$RichText;

    .line 649
    iget-object v1, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    if-eqz v1, :cond_3c

    iget-object v0, v0, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->has()Z

    move-result v0

    if-eqz v0, :cond_3c

    .line 650
    const/4 v0, 0x1

    .line 651
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint32_bubble_diy_text_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v3, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipBubbleDiyTextId:I

    invoke-virtual {v1, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 652
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_10

    .line 653
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v3, 0x2

    const-string v5, "add BubbleDiyTextId!"

    invoke-static {v1, v3, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 660
    :cond_10
    :goto_3
    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    if-eqz v1, :cond_12

    .line 661
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_11

    .line 662
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    const-string v5, "getGeneralFlagElemFromMsg, sub bubbleid: "

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    invoke-virtual {v3, v5}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v0, v1, v3}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 664
    :cond_11
    const/4 v0, 0x1

    .line 665
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint32_bubble_sub_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v3, p1, Lcom/tencent/mobileqq/data/MessageRecord;->vipSubBubbleId:I

    invoke-virtual {v1, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    :cond_12
    move v3, v0

    .line 672
    :cond_13
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;

    if-eqz v0, :cond_17

    invoke-static {p0}, Lagvx;->a(Lcom/tencent/mobileqq/app/QQAppInterface;)Z

    move-result v0

    if-eqz v0, :cond_17

    .line 673
    invoke-static {p0}, Lagvx;->b(Lcom/tencent/mobileqq/app/QQAppInterface;)Z

    move-result v7

    .line 674
    const/16 v0, 0x7d

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lagvx;

    move-object v1, p1

    .line 675
    check-cast v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;

    .line 676
    iget-object v5, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagId:Ljava/lang/String;

    if-eqz v5, :cond_16

    invoke-static {p0}, Lagvx;->b(Lcom/tencent/mobileqq/app/QQAppInterface;)Z

    move-result v5

    if-eqz v5, :cond_16

    .line 677
    const/4 v3, 0x1

    .line 678
    const/4 v2, 0x1

    .line 679
    if-eqz v7, :cond_24

    iget-boolean v0, v0, Lagvx;->e:Z

    if-nez v0, :cond_24

    const/4 v0, 0x1

    move v5, v0

    .line 680
    :goto_4
    if-nez v5, :cond_14

    .line 682
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint64_uin:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->mPasswdRedBagSender:J

    invoke-virtual {v0, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 684
    :cond_14
    iget-object v8, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint32_prp_fold:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget-boolean v0, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->foldFlagTemp:Z

    if-eqz v0, :cond_25

    const/4 v0, 0x1

    :goto_5
    invoke-virtual {v8, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 686
    iget-object v0, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagId:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_15

    if-nez v5, :cond_15

    .line 687
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->bytes_rp_id:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v5, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagId:Ljava/lang/String;

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 690
    :cond_15
    iget-object v0, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagIndex:Ljava/lang/String;

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_16

    if-eqz v7, :cond_16

    .line 691
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->bytes_rp_index:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v5, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagIndex:Ljava/lang/String;

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 692
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->redbag_msg_sender_uin:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->mPasswdRedBagSender:J

    invoke-virtual {v0, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 696
    :cond_16
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_17

    .line 697
    const-string v5, "msgFold"

    const/4 v7, 0x2

    const-string v8, "Send, pack foldMsg, senderUin: %d, foldFlag: %s, redBagId: %s"

    const/4 v0, 0x3

    new-array v9, v0, [Ljava/lang/Object;

    const/4 v0, 0x0

    iget-wide v10, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->mPasswdRedBagSender:J

    .line 698
    invoke-static {v10, v11}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v10

    aput-object v10, v9, v0

    const/4 v10, 0x1

    iget-boolean v0, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->foldFlagTemp:Z

    if-eqz v0, :cond_26

    const/4 v0, 0x1

    :goto_6
    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    aput-object v0, v9, v10

    const/4 v0, 0x2

    iget-object v10, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->redBagId:Ljava/lang/String;

    aput-object v10, v9, v0

    .line 697
    invoke-static {v8, v9}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v5, v7, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 699
    const-string v0, "msgFold"

    const/4 v5, 0x2

    const-string v7, "Send, send foldMsg, frienduin: %s, msguid: %s, shmsgseq: %s content: %s"

    const/4 v8, 0x4

    new-array v8, v8, [Ljava/lang/Object;

    const/4 v9, 0x0

    iget-object v10, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->frienduin:Ljava/lang/String;

    aput-object v10, v8, v9

    const/4 v9, 0x1

    iget-wide v10, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->msgUid:J

    .line 700
    invoke-static {v10, v11}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v10

    aput-object v10, v8, v9

    const/4 v9, 0x2

    iget-wide v10, v1, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->shmsgseq:J

    invoke-static {v10, v11}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v10

    aput-object v10, v8, v9

    const/4 v9, 0x3

    invoke-virtual {v1}, Lcom/tencent/mobileqq/data/MessageForFoldMsg;->getLogColorContent()Ljava/lang/String;

    move-result-object v1

    aput-object v1, v8, v9

    .line 699
    invoke-static {v7, v8}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v5, v1}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 705
    :cond_17
    iget v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v1, -0x80a

    if-ne v0, v1, :cond_18

    .line 706
    invoke-static {p1}, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;

    move-result-object v0

    .line 707
    if-eqz v0, :cond_18

    .line 708
    const/4 v2, 0x1

    .line 709
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->float_sticker_x:Lcom/tencent/mobileqq/pb/PBFloatField;

    iget v5, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->x:F

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBFloatField;->set(F)V

    .line 710
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->float_sticker_y:Lcom/tencent/mobileqq/pb/PBFloatField;

    iget v5, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->y:F

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBFloatField;->set(F)V

    .line 711
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->float_sticker_width:Lcom/tencent/mobileqq/pb/PBFloatField;

    iget v5, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->width:F

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBFloatField;->set(F)V

    .line 712
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->float_sticker_height:Lcom/tencent/mobileqq/pb/PBFloatField;

    iget v5, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->height:F

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBFloatField;->set(F)V

    .line 713
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_sticker_rotate:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->rotate:I

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 715
    iget v1, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    if-nez v1, :cond_27

    .line 716
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_sticker_host_msgseq:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->hostMsgSeq:J

    long-to-int v5, v8

    int-to-short v5, v5

    const v7, 0xffff

    and-int/2addr v5, v7

    int-to-long v8, v5

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 721
    :goto_7
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_sticker_host_msguid:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->hostMsgUid:J

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 722
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_sticker_host_time:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->hostMsgTime:J

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 723
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_18

    .line 724
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x2

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string/jumbo v8, "set generalFlag for sticker info: "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v0}, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v5, v0}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 730
    :cond_18
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForLongTextMsg;

    if-eqz v0, :cond_28

    move-object v0, p1

    .line 731
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForLongTextMsg;

    .line 732
    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForLongTextMsg;->structingMsg:Lcom/tencent/mobileqq/structmsg/AbsStructMsg;

    if-eqz v1, :cond_19

    .line 733
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v3, 0x1

    invoke-virtual {v1, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 736
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_resid:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForLongTextMsg;->structingMsg:Lcom/tencent/mobileqq/structmsg/AbsStructMsg;

    iget-object v0, v0, Lcom/tencent/mobileqq/structmsg/AbsStructMsg;->mResid:Ljava/lang/String;

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 737
    const/4 v3, 0x1

    .line 759
    :cond_19
    :goto_8
    const/16 v0, 0x33

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lajxl;

    .line 760
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->c()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lajxl;->a(Ljava/lang/String;)Lcom/tencent/mobileqq/data/ExtensionInfo;

    move-result-object v1

    .line 761
    if-eqz v1, :cond_1f

    .line 762
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->uint64_pendant_id:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v2, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->pendantId:J

    invoke-virtual {v0, v2, v3}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 763
    const/4 v3, 0x1

    .line 764
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_mobile_custom_font:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-static {v1}, Lho;->a(Lcom/tencent/mobileqq/data/ExtensionInfo;)I

    move-result v2

    invoke-virtual {v0, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 765
    const-string/jumbo v0, "vip_sub_font_id"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v2

    .line 766
    const/4 v0, 0x0

    .line 767
    invoke-static {v2}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-nez v5, :cond_1a

    .line 768
    invoke-static {v2}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    .line 770
    :cond_1a
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v2

    if-eqz v2, :cond_1b

    .line 771
    const-string v2, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x2

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "getGeneralFlagElemFromMsg: fontId="

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-wide v8, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->uVipFont:J

    invoke-virtual {v7, v8, v9}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v7

    const-string v8, " subFontId="

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-static {v2, v5, v7}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 773
    :cond_1b
    iget-object v2, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_subfont_id:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    int-to-long v8, v0

    invoke-virtual {v2, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 774
    iget-object v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->diyFontTimestampMap:Ljava/util/concurrent/ConcurrentHashMap;

    if-eqz v0, :cond_1c

    .line 775
    iget-object v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->diyFontTimestampMap:Ljava/util/concurrent/ConcurrentHashMap;

    iget-wide v8, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->uVipFont:J

    long-to-int v2, v8

    invoke-static {v2}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    if-eqz v0, :cond_1c

    .line 776
    iget-object v2, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_diy_font_timestamp:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget-object v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->diyFontTimestampMap:Ljava/util/concurrent/ConcurrentHashMap;

    iget-wide v8, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->uVipFont:J

    long-to-int v5, v8

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual {v0, v5}, Ljava/util/concurrent/ConcurrentHashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    invoke-virtual {v2, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 779
    :cond_1c
    const/4 v2, 0x1

    .line 782
    iget v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->pendantDiyId:I

    if-lez v0, :cond_1d

    .line 783
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_pendant_diy_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->pendantDiyId:I

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 787
    :cond_1d
    iget v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->faceId:I

    if-lez v0, :cond_1e

    .line 788
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_face_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->faceId:I

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 792
    :cond_1e
    iget v0, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->fontEffect:I

    if-lez v0, :cond_1f

    .line 793
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_req_font_effect_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v1, v1, Lcom/tencent/mobileqq/data/ExtensionInfo;->fontEffect:I

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 797
    :cond_1f
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForWantGiftMsg;

    if-eqz v0, :cond_20

    .line 798
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->want_gift_sender_uin:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    move-object v0, p1

    check-cast v0, Lcom/tencent/mobileqq/data/MessageForWantGiftMsg;

    iget-wide v8, v0, Lcom/tencent/mobileqq/data/MessageForWantGiftMsg;->wantGiftSenderUin:J

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 799
    const/4 v2, 0x1

    .line 801
    :cond_20
    const-string/jumbo v0, "robot_news_class_id"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 803
    :try_start_4
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_bot_message_class_id:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V
    :try_end_4
    .catch Ljava/lang/Exception; {:try_start_4 .. :try_end_4} :catch_5

    .line 808
    :goto_9
    const-string v0, "key_message_extra_info_flag"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    .line 809
    const/4 v0, 0x0

    .line 811
    :try_start_5
    invoke-static {v5}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I
    :try_end_5
    .catch Ljava/lang/NumberFormatException; {:try_start_5 .. :try_end_5} :catch_2

    move-result v0

    move v1, v0

    .line 815
    :goto_a
    and-int/lit8 v0, v1, 0x1

    const/4 v7, 0x1

    if-ne v0, v7, :cond_2a

    const/4 v0, 0x1

    .line 816
    :goto_b
    if-eqz v0, :cond_21

    .line 817
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_msg_info_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 818
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_21

    .line 819
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "getGeneralFlagElemFromMsg: invoked.  containPasteExtra: "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    invoke-virtual {v7, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v0, v1, v5}, Lcom/tencent/qphone/base/util/QLog;->i(Ljava/lang/String;ILjava/lang/String;)V

    .line 823
    :cond_21
    const/16 v0, 0xcb

    invoke-virtual {p0, v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->getManager(I)Lmqq/manager/Manager;

    move-result-object v0

    check-cast v0, Lbamk;

    .line 824
    invoke-virtual {v0, p1}, Lbamk;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-eqz v0, :cond_2b

    .line 825
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_robot_msg_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v1, 0x1

    const/4 v2, 0x1

    invoke-virtual {v0, v1, v2}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(IZ)V

    .line 826
    const/4 v2, 0x1

    .line 828
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPtt;

    if-eqz v0, :cond_2b

    move-object v0, p1

    .line 829
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPtt;

    .line 830
    new-instance v1, Ltencent/im/msg/im_msg_body$Elem;

    invoke-direct {v1}, Ltencent/im/msg/im_msg_body$Elem;-><init>()V

    .line 831
    new-instance v1, Ltencent/im/msg/im_msg_body$Text;

    invoke-direct {v1}, Ltencent/im/msg/im_msg_body$Text;-><init>()V

    .line 832
    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->atInfoList:Ljava/util/ArrayList;

    if-eqz v1, :cond_2b

    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->atInfoList:Ljava/util/ArrayList;

    invoke-virtual {v1}, Ljava/util/ArrayList;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_2b

    .line 833
    iget-object v1, p2, Lmsf/msgsvc/msg_svc$PbSendMsgReq;->msg_body:Ltencent/im/msg/im_msg_body$MsgBody;

    invoke-virtual {v1}, Ltencent/im/msg/im_msg_body$MsgBody;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v1

    check-cast v1, Ltencent/im/msg/im_msg_body$MsgBody;

    iget-object v1, v1, Ltencent/im/msg/im_msg_body$MsgBody;->rich_text:Ltencent/im/msg/im_msg_body$RichText;

    invoke-virtual {v1}, Ltencent/im/msg/im_msg_body$RichText;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v1

    check-cast v1, Ltencent/im/msg/im_msg_body$RichText;

    .line 834
    iget-object v5, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->mInputContent:Ljava/lang/String;

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->atInfoList:Ljava/util/ArrayList;

    invoke-static {v1, v5, v0}, Laxaq;->a(Ltencent/im/msg/im_msg_body$RichText;Ljava/lang/String;Ljava/util/ArrayList;)I

    .line 837
    iget-object v0, v1, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->has()Z

    move-result v0

    if-eqz v0, :cond_2b

    .line 838
    iget-object v0, v1, Ltencent/im/msg/im_msg_body$RichText;->elems:Lcom/tencent/mobileqq/pb/PBRepeatMessageField;

    invoke-virtual {v0}, Lcom/tencent/mobileqq/pb/PBRepeatMessageField;->get()Ljava/util/List;

    move-result-object v0

    .line 839
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_22
    :goto_c
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2b

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$Elem;

    .line 840
    iget-object v5, v0, Ltencent/im/msg/im_msg_body$Elem;->text:Ltencent/im/msg/im_msg_body$Text;

    invoke-virtual {v5}, Ltencent/im/msg/im_msg_body$Text;->has()Z

    move-result v5

    if-eqz v5, :cond_22

    .line 841
    iget-object v0, v0, Ltencent/im/msg/im_msg_body$Elem;->text:Ltencent/im/msg/im_msg_body$Text;

    invoke-virtual {v0}, Ltencent/im/msg/im_msg_body$Text;->get()Lcom/tencent/mobileqq/pb/MessageMicro;

    move-result-object v0

    check-cast v0, Ltencent/im/msg/im_msg_body$Text;

    .line 842
    iget-object v5, v0, Ltencent/im/msg/im_msg_body$Text;->str:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->has()Z

    move-result v5

    if-eqz v5, :cond_22

    .line 843
    iget-object v0, v0, Ltencent/im/msg/im_msg_body$Text;->str:Lcom/tencent/mobileqq/pb/PBBytesField;

    const-string v5, ""

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v0, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    goto :goto_c

    .line 641
    :cond_23
    const/4 v0, 0x0

    goto/16 :goto_2

    .line 679
    :cond_24
    const/4 v0, 0x0

    move v5, v0

    goto/16 :goto_4

    .line 684
    :cond_25
    const/4 v0, 0x0

    goto/16 :goto_5

    .line 698
    :cond_26
    const/4 v0, 0x0

    goto/16 :goto_6

    .line 718
    :cond_27
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_sticker_host_msgseq:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, v0, Lcom/tencent/mobileqq/emoticon/EmojiStickerManager$StickerInfo;->hostMsgSeq:J

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    goto/16 :goto_7

    .line 740
    :cond_28
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForArkApp;

    if-eqz v0, :cond_29

    move-object v0, p1

    .line 742
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForArkApp;

    .line 743
    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForArkApp;->resIDForLongMsg:Ljava/lang/String;

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_19

    .line 745
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v3, 0x1

    invoke-virtual {v1, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 746
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_resid:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForArkApp;->resIDForLongMsg:Ljava/lang/String;

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 747
    const/4 v3, 0x1

    goto/16 :goto_8

    .line 750
    :cond_29
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForArkBabyqReply;

    if-eqz v0, :cond_19

    move-object v0, p1

    .line 751
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForArkBabyqReply;

    .line 752
    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForArkBabyqReply;->resIDForLongMsg:Ljava/lang/String;

    invoke-static {v1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v1

    if-nez v1, :cond_19

    .line 753
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v3, 0x1

    invoke-virtual {v1, v3}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 754
    iget-object v1, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->long_text_resid:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForArkBabyqReply;->resIDForLongMsg:Ljava/lang/String;

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 755
    const/4 v3, 0x1

    goto/16 :goto_8

    .line 812
    :catch_2
    move-exception v1

    .line 813
    invoke-virtual {v1}, Ljava/lang/NumberFormatException;->printStackTrace()V

    move v1, v0

    goto/16 :goto_a

    .line 815
    :cond_2a
    const/4 v0, 0x0

    goto/16 :goto_b

    .line 852
    :cond_2b
    invoke-static {p1}, Lanoi;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-eqz v0, :cond_2c

    .line 853
    const/4 v2, 0x1

    .line 854
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_doutu_msg_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 855
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2c

    .line 856
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    const-string v5, "getGeneralFlagElemFromMsg [doutu] set uint32_doutu_msg_type "

    invoke-static {v0, v1, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 860
    :cond_2c
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPic;

    if-eqz v0, :cond_2d

    move-object v0, p1

    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    invoke-static {v0}, Laezl;->b(Lcom/tencent/mobileqq/data/MessageForPic;)Z

    move-result v0

    if-eqz v0, :cond_2d

    .line 861
    const/4 v2, 0x1

    .line 862
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_custom_featureid:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const v1, 0x8101

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 863
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2d

    .line 864
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    const-string v5, "getGeneralFlagElemFromMsg [zhitu] set uint32_custom_featureid "

    invoke-static {v0, v1, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 869
    :cond_2d
    instance-of v0, p1, Lcom/tencent/mobileqq/data/ChatMessage;

    if-eqz v0, :cond_2f

    const-string v0, "msg_ext_key"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v0

    if-nez v0, :cond_2f

    .line 870
    const/4 v1, 0x0

    .line 872
    :try_start_6
    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->selfuin:Ljava/lang/String;

    invoke-static {p0, v0}, Lcom/tencent/mobileqq/data/KplRoleInfo;->getGameNickByUin(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 873
    iget-object v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-static {p0, v5}, Lcom/tencent/mobileqq/data/KplRoleInfo;->getGameNickByUin(Lcom/tencent/mobileqq/app/QQAppInterface;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    .line 874
    new-instance v7, Lorg/json/JSONObject;

    invoke-direct {v7}, Lorg/json/JSONObject;-><init>()V

    .line 875
    iget-object v8, p1, Lcom/tencent/mobileqq/data/MessageRecord;->selfuin:Ljava/lang/String;

    invoke-virtual {v7, v8, v0}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 876
    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-virtual {v7, v0, v5}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 877
    invoke-virtual {v7}, Lorg/json/JSONObject;->toString()Ljava/lang/String;

    move-result-object v0

    .line 878
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v5

    if-eqz v5, :cond_2e

    .line 879
    const-string v5, "KplMessage"

    const/4 v7, 0x2

    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    const-string v9, "general kpl flag="

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-static {v5, v7, v8}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 881
    :cond_2e
    const-string/jumbo v5, "utf-8"

    invoke-virtual {v0, v5}, Ljava/lang/String;->getBytes(Ljava/lang/String;)[B
    :try_end_6
    .catch Ljava/lang/Exception; {:try_start_6 .. :try_end_6} :catch_3

    move-result-object v0

    .line 889
    :goto_d
    if-eqz v0, :cond_2f

    .line 890
    const/4 v2, 0x1

    .line 891
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_kpl_info:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 895
    :cond_2f
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    if-eqz v0, :cond_31

    move-object v0, p1

    .line 896
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;

    .line 897
    iget v1, v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;->redBagType:I

    sget v5, Lcom/tencent/mobileqq/activity/photo/LocalMediaInfo;->REDBAG_TYPE_GET:I

    if-ne v1, v5, :cond_30

    .line 898
    const/4 v2, 0x1

    .line 899
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_red_envelope_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget v5, v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;->redBagType:I

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 900
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_shortVideoId:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v5, v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;->shortVideoId:Ljava/lang/String;

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 901
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x1

    const-string v7, "VideoRedbag, generalflag take redbag flag"

    invoke-static {v1, v5, v7}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 905
    :cond_30
    iget-object v5, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_aio_sync_to_story_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget-boolean v1, v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;->syncToStory:Z

    if-eqz v1, :cond_39

    const/4 v1, 0x1

    :goto_e
    invoke-virtual {v5, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 906
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x1

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "EditAioSyncToStoryPart Video sync to story is "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-boolean v0, v0, Lcom/tencent/mobileqq/data/MessageForShortVideo;->syncToStory:Z

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v5, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 908
    :cond_31
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPic;

    if-eqz v0, :cond_32

    move-object v0, p1

    .line 909
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPic;

    .line 910
    iget-object v5, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_aio_sync_to_story_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    iget-boolean v1, v0, Lcom/tencent/mobileqq/data/MessageForPic;->sync2Story:Z

    if-eqz v1, :cond_3a

    const/4 v1, 0x1

    :goto_f
    invoke-virtual {v5, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 911
    const-string v1, "Q.msg.MessageHandlerUtils"

    const/4 v5, 0x1

    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    const-string v8, "EditAioSyncToStoryPart pic sync to story is "

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-boolean v0, v0, Lcom/tencent/mobileqq/data/MessageForPic;->sync2Story:Z

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v5, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 915
    :cond_32
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPic;

    if-eqz v0, :cond_33

    .line 916
    const-string v0, "msg_extra_key_is_sync_qzone"

    invoke-virtual {p1, v0}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    const-string v1, "1"

    invoke-virtual {v0, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_33

    .line 917
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_upload_image_to_qzone_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 919
    :try_start_7
    new-instance v0, Ltencent/im/qzone/album/UploadParam;

    invoke-direct {v0}, Ltencent/im/qzone/album/UploadParam;-><init>()V

    .line 920
    iget-object v1, v0, Ltencent/im/qzone/album/UploadParam;->bytes_qzone_albumid:Lcom/tencent/mobileqq/pb/PBBytesField;

    const-string v5, "msg_extra_key_qzone_album_id"

    invoke-virtual {p1, v5}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFromUtf8(Ljava/lang/String;)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v5

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 921
    iget-object v1, v0, Ltencent/im/qzone/album/UploadParam;->uint64_batchid:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    const-string v5, "msg_extra_key_qzone_batch_id"

    invoke-virtual {p1, v5}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Long;->valueOf(Ljava/lang/String;)Ljava/lang/Long;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/Long;->longValue()J

    move-result-wide v8

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 922
    iget-object v1, v0, Ltencent/im/qzone/album/UploadParam;->uint32_full_image:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const-string v5, "msg_extra_key_qzone_is_raw"

    invoke-virtual {p1, v5}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 923
    iget-object v1, v0, Ltencent/im/qzone/album/UploadParam;->uint64_image_number:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    const-string v5, "msg_extra_key_qzone_batch_count"

    invoke-virtual {p1, v5}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Long;->valueOf(Ljava/lang/String;)Ljava/lang/Long;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/Long;->longValue()J

    move-result-wide v8

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 924
    iget-object v1, v0, Ltencent/im/qzone/album/UploadParam;->uint64_image_number_index:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    const-string v5, "msg_extra_key_qzone_photo_index"

    invoke-virtual {p1, v5}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Long;->valueOf(Ljava/lang/String;)Ljava/lang/Long;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/Long;->longValue()J

    move-result-wide v8

    invoke-virtual {v1, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 925
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_upload_image_to_qzone_param:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/qzone/album/UploadParam;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 926
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_33

    .line 927
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "general syncQzoneAlbum : albumid = "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, "msg_extra_key_qzone_album_id"

    .line 928
    invoke-virtual {p1, v7}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, ", batchid = "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, "msg_extra_key_qzone_batch_id"

    .line 929
    invoke-virtual {p1, v7}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, ", isRaw = "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, "msg_extra_key_qzone_is_raw"

    .line 930
    invoke-virtual {p1, v7}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, ", imgCount = "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, "msg_extra_key_qzone_batch_count"

    .line 931
    invoke-virtual {p1, v7}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, ", imgIndex = "

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    const-string v7, "msg_extra_key_qzone_photo_index"

    .line 932
    invoke-virtual {p1, v7}, Lcom/tencent/mobileqq/data/MessageRecord;->getExtInfoFromExtStr(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v5, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    .line 927
    invoke-static {v0, v1, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V
    :try_end_7
    .catch Ljava/lang/Exception; {:try_start_7 .. :try_end_7} :catch_4

    .line 941
    :cond_33
    :goto_10
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForPtt;

    if-eqz v0, :cond_34

    move-object v0, p1

    .line 942
    check-cast v0, Lcom/tencent/mobileqq/data/MessageForPtt;

    .line 943
    iget-object v1, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->waveformArray:[I

    if-eqz v1, :cond_34

    .line 944
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_pb_ptt_waveform:Lcom/tencent/mobileqq/pb/PBBytesField;

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageForPtt;->waveformArray:[I

    invoke-static {v0}, Lbbga;->a([I)Ltencent/im/cs/ptt_waveform/ptt_waveform$PttWaveform;

    move-result-object v0

    invoke-virtual {v0}, Ltencent/im/cs/ptt_waveform/ptt_waveform$PttWaveform;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 948
    :cond_34
    instance-of v0, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;

    if-eqz v0, :cond_36

    .line 949
    const/4 v2, 0x1

    .line 950
    check-cast p1, Lcom/tencent/mobileqq/data/MessageForReplyText;

    .line 951
    iget-boolean v0, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->isBarrageMsg:Z

    if-eqz v0, :cond_35

    .line 952
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint32_comment_flag:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v1, 0x1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 953
    iget-object v0, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->uint64_comment_location:Lcom/tencent/mobileqq/pb/PBUInt64Field;

    iget-wide v8, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->barrageTimeLocation:J

    invoke-virtual {v0, v8, v9}, Lcom/tencent/mobileqq/pb/PBUInt64Field;->set(J)V

    .line 955
    new-instance v0, Ltencent/im/oidb/oidb_0xdea$PassThrough;

    invoke-direct {v0}, Ltencent/im/oidb/oidb_0xdea$PassThrough;-><init>()V

    .line 956
    iget-object v1, v0, Ltencent/im/oidb/oidb_0xdea$PassThrough;->entrance:Lcom/tencent/mobileqq/pb/PBEnumField;

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->barrageSourceMsgType:I

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBEnumField;->set(I)V

    .line 957
    iget-object v1, v0, Ltencent/im/oidb/oidb_0xdea$PassThrough;->uint32_client_type:Lcom/tencent/mobileqq/pb/PBUInt32Field;

    const/4 v5, 0x1

    invoke-virtual {v1, v5}, Lcom/tencent/mobileqq/pb/PBUInt32Field;->set(I)V

    .line 958
    iget-object v1, v6, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->bytes_pass_through:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v0}, Ltencent/im/oidb/oidb_0xdea$PassThrough;->toByteArray()[B

    move-result-object v0

    invoke-static {v0}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v0

    invoke-virtual {v1, v0}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 960
    :cond_35
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_36

    .line 961
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    const/4 v5, 0x6

    new-array v5, v5, [Ljava/lang/Object;

    const/4 v7, 0x0

    const-string/jumbo v8, "send replay msg, isBarrageMsg: "

    aput-object v8, v5, v7

    const/4 v7, 0x1

    iget-boolean v8, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->isBarrageMsg:Z

    invoke-static {v8}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v8

    aput-object v8, v5, v7

    const/4 v7, 0x2

    const-string v8, " timeLocation:"

    aput-object v8, v5, v7

    const/4 v7, 0x3

    iget-wide v8, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->barrageTimeLocation:J

    .line 962
    invoke-static {v8, v9}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v8

    aput-object v8, v5, v7

    const/4 v7, 0x4

    const-string v8, " type:"

    aput-object v8, v5, v7

    const/4 v7, 0x5

    iget v8, p1, Lcom/tencent/mobileqq/data/MessageForReplyText;->barrageSourceMsgType:I

    invoke-static {v8}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v8

    aput-object v8, v5, v7

    .line 961
    invoke-static {v0, v1, v5}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;I[Ljava/lang/Object;)V

    .line 967
    :cond_36
    if-eqz v2, :cond_37

    .line 968
    const/4 v3, 0x1

    .line 969
    iget-object v0, v4, Ltencent/im/msg/im_msg_body$GeneralFlags;->bytes_pb_reserve:Lcom/tencent/mobileqq/pb/PBBytesField;

    invoke-virtual {v6}, Ltencent/im/msg/hummer/resv/generalflags$ResvAttr;->toByteArray()[B

    move-result-object v1

    invoke-static {v1}, Lcom/tencent/mobileqq/pb/ByteStringMicro;->copyFrom([B)Lcom/tencent/mobileqq/pb/ByteStringMicro;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/tencent/mobileqq/pb/PBBytesField;->set(Lcom/tencent/mobileqq/pb/ByteStringMicro;)V

    .line 971
    :cond_37
    if-eqz v3, :cond_3b

    move-object v0, v4

    :goto_11
    move-object v4, v0

    goto/16 :goto_1

    .line 882
    :catch_3
    move-exception v0

    .line 883
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v5

    if-eqz v5, :cond_38

    .line 884
    const-string v5, "KplMessage"

    const/4 v7, 0x2

    const-string/jumbo v8, "some error at MessageHandlerUtils::getGeneralFlagElemFromMsg()"

    invoke-static {v5, v7, v8}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 886
    :cond_38
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    move-object v0, v1

    goto/16 :goto_d

    .line 905
    :cond_39
    const/4 v1, 0x0

    goto/16 :goto_e

    .line 910
    :cond_3a
    const/4 v1, 0x0

    goto/16 :goto_f

    .line 971
    :cond_3b
    const/4 v0, 0x0

    goto :goto_11

    .line 934
    :catch_4
    move-exception v0

    goto/16 :goto_10

    .line 804
    :catch_5
    move-exception v0

    goto/16 :goto_9

    :cond_3c
    move v0, v3

    goto/16 :goto_3

    :cond_3d
    move v0, v1

    goto/16 :goto_2

    :cond_3e
    move v2, v0

    goto/16 :goto_0
.end method

.method public static a(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;)V
    .locals 4

    .prologue
    const/4 v2, 0x2

    .line 187
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 188
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "<"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p2, p3}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v2, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 189
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0, p2, p3}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string/jumbo v1, "|"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string/jumbo v1, "|"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    .line 190
    if-eqz p1, :cond_0

    .line 191
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v2, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 194
    :cond_0
    return-void
.end method

.method public static a(Ljava/util/List;)V
    .locals 10
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/List",
            "<",
            "Lcom/tencent/mobileqq/data/MessageRecord;",
            ">;)V"
        }
    .end annotation

    .prologue
    const/4 v4, 0x1

    const/4 v3, 0x0

    .line 104
    if-eqz p0, :cond_0

    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    if-gt v0, v4, :cond_1

    .line 119
    :cond_0
    :goto_0
    return-void

    .line 107
    :cond_1
    new-instance v5, Ljava/util/ArrayList;

    invoke-direct {v5}, Ljava/util/ArrayList;-><init>()V

    move v2, v3

    .line 108
    :goto_1
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v0

    if-ge v2, v0, :cond_6

    .line 109
    invoke-interface {p0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 110
    invoke-static {v0}, Laxau;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v1

    if-eqz v1, :cond_4

    .line 111
    if-lez v2, :cond_2

    add-int/lit8 v1, v2, -0x1

    invoke-interface {p0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/tencent/mobileqq/data/MessageRecord;

    iget-wide v6, v1, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    iget-wide v8, v0, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    cmp-long v1, v6, v8

    if-eqz v1, :cond_3

    :cond_2
    invoke-interface {p0}, Ljava/util/List;->size()I

    move-result v1

    add-int/lit8 v1, v1, -0x1

    if-ge v2, v1, :cond_5

    add-int/lit8 v1, v2, 0x1

    invoke-interface {p0, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/tencent/mobileqq/data/MessageRecord;

    iget-wide v6, v1, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    iget-wide v8, v0, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    cmp-long v1, v6, v8

    if-nez v1, :cond_5

    :cond_3
    move v1, v4

    .line 112
    :goto_2
    if-eqz v1, :cond_4

    .line 113
    invoke-interface {v5, v0}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    .line 108
    :cond_4
    add-int/lit8 v0, v2, 0x1

    move v2, v0

    goto :goto_1

    :cond_5
    move v1, v3

    .line 111
    goto :goto_2

    .line 118
    :cond_6
    invoke-interface {p0, v5}, Ljava/util/List;->removeAll(Ljava/util/Collection;)Z

    goto :goto_0
.end method

.method public static a(I)Z
    .locals 2

    .prologue
    const/4 v0, 0x1

    .line 1482
    if-eq p0, v0, :cond_0

    const/4 v1, 0x3

    if-eq p0, v1, :cond_0

    const/16 v1, 0x18

    if-eq p0, v1, :cond_0

    const/16 v1, 0xe

    if-eq p0, v1, :cond_0

    const/4 v1, 0x5

    if-eq p0, v1, :cond_0

    const/4 v1, 0x6

    if-eq p0, v1, :cond_0

    const/16 v1, 0xa

    if-eq p0, v1, :cond_0

    const/16 v1, 0xb

    if-eq p0, v1, :cond_0

    const/16 v1, 0xc

    if-eq p0, v1, :cond_0

    const/16 v1, 0x17

    if-eq p0, v1, :cond_0

    const/16 v1, 0x19

    if-eq p0, v1, :cond_0

    const/16 v1, 0x1a

    if-eq p0, v1, :cond_0

    const/16 v1, 0x8

    if-eq p0, v1, :cond_0

    const/16 v1, 0x10

    if-eq p0, v1, :cond_0

    const/16 v1, 0x11

    if-eq p0, v1, :cond_0

    const/16 v1, 0x12

    if-eq p0, v1, :cond_0

    const/16 v1, 0xf

    if-eq p0, v1, :cond_0

    const/16 v1, 0x13

    if-eq p0, v1, :cond_0

    const/16 v1, 0x14

    if-eq p0, v1, :cond_0

    const/16 v1, 0x16

    if-eq p0, v1, :cond_0

    const/16 v1, 0x1c

    if-eq p0, v1, :cond_0

    const/16 v1, 0x1b

    if-eq p0, v1, :cond_0

    const/16 v1, 0x1d

    if-ne p0, v1, :cond_1

    :cond_0
    :goto_0
    return v0

    :cond_1
    const/4 v0, 0x0

    goto :goto_0
.end method

.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;ILjava/lang/String;Ljava/lang/String;JJ)Z
    .locals 6

    .prologue
    .line 1333
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lcom/tencent/mobileqq/app/message/QQMessageFacade;

    move-result-object v0

    invoke-virtual {v0, p2, p1}, Lcom/tencent/mobileqq/app/message/QQMessageFacade;->b(Ljava/lang/String;I)Ljava/util/List;

    move-result-object v1

    .line 1334
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 1335
    const-string v2, "Q.msg.MessageHandlerUtils"

    const/4 v3, 0x2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "------->msgFilter :msgIsTroop:"

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p1}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v4, ",peerUin:"

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v4, ",list size:"

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    if-nez v1, :cond_3

    const/4 v0, 0x0

    :goto_0
    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v2, v3, v0}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1338
    :cond_0
    if-eqz v1, :cond_4

    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v0

    if-lez v0, :cond_4

    .line 1340
    invoke-interface {v1}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_1
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_4

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1344
    iget-wide v2, v0, Lcom/tencent/mobileqq/data/MessageRecord;->time:J

    sub-long/2addr v2, p4

    invoke-static {v2, v3}, Ljava/lang/Math;->abs(J)J

    move-result-wide v2

    const-wide/16 v4, 0x1e

    cmp-long v2, v2, v4

    if-gez v2, :cond_1

    iget-wide v2, v0, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    cmp-long v2, v2, p6

    if-nez v2, :cond_1

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageRecord;->senderuin:Ljava/lang/String;

    invoke-virtual {v0, p3}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_1

    .line 1346
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2

    .line 1347
    const-string v0, "Q.msg.MessageHandlerUtils"

    const/4 v1, 0x2

    const-string v2, "------->msgFilter-duplicated msg."

    invoke-static {v0, v1, v2}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1349
    :cond_2
    const/4 v0, 0x1

    .line 1354
    :goto_1
    return v0

    .line 1335
    :cond_3
    invoke-interface {v1}, Ljava/util/List;->size()I

    move-result v0

    goto :goto_0

    .line 1354
    :cond_4
    const/4 v0, 0x0

    goto :goto_1
.end method

.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;)Z
    .locals 12

    .prologue
    const/4 v3, 0x1

    const/4 v11, 0x2

    const/4 v2, 0x0

    .line 1150
    if-nez p1, :cond_1

    .line 1151
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_0

    .line 1152
    const-string v0, "Q.msg.MessageHandlerUtils"

    const-string v1, "---------------msgFilter message [before filter] is null !"

    invoke-static {v0, v11, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    :cond_0
    move v2, v3

    .line 1190
    :goto_0
    return v2

    .line 1157
    :cond_1
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    .line 1159
    const/4 v0, 0x0

    .line 1160
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_8

    .line 1161
    new-instance v0, Ljava/lang/StringBuilder;

    const/16 v1, 0x100

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(I)V

    .line 1162
    const-string v1, "---------------msgFilter istroop: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " shmsgseq: "

    .line 1163
    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-wide v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    invoke-virtual {v1, v4, v5}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " friendUin: "

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " senderUin: "

    .line 1164
    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->senderuin:Ljava/lang/String;

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " msgType: "

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " time:"

    .line 1165
    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-wide v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->time:J

    invoke-virtual {v1, v4, v5}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v4, " msgContent: "

    .line 1166
    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1}, Lcom/tencent/mobileqq/data/MessageRecord;->getLogColorContent()Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-object v1, v0

    .line 1169
    :goto_1
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lcom/tencent/mobileqq/app/message/QQMessageFacade;

    move-result-object v0

    iget-object v4, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    iget v5, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-virtual {v0, v4, v5}, Lcom/tencent/mobileqq/app/message/QQMessageFacade;->b(Ljava/lang/String;I)Ljava/util/List;

    move-result-object v8

    .line 1170
    if-nez v8, :cond_3

    move v5, v2

    .line 1171
    :goto_2
    const/16 v0, 0xf

    if-le v5, v0, :cond_4

    add-int/lit8 v0, v5, -0xf

    :goto_3
    move v4, v0

    .line 1172
    :goto_4
    if-ge v4, v5, :cond_6

    .line 1173
    invoke-interface {v8, v4}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1174
    if-eqz v0, :cond_5

    iget v9, v0, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    iget v10, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    if-ne v9, v10, :cond_5

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    iget-object v9, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    .line 1175
    invoke-static {v0, v9}, Lbbbr;->a(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_5

    .line 1176
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_2

    if-eqz v1, :cond_2

    .line 1177
    const-string v0, " filterType: other"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1178
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v11, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    .line 1181
    :cond_2
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    sub-long/2addr v4, v6

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v2, v3

    .line 1182
    goto/16 :goto_0

    .line 1170
    :cond_3
    invoke-interface {v8}, Ljava/util/List;->size()I

    move-result v0

    move v5, v0

    goto :goto_2

    :cond_4
    move v0, v2

    .line 1171
    goto :goto_3

    .line 1172
    :cond_5
    add-int/lit8 v0, v4, 0x1

    move v4, v0

    goto :goto_4

    .line 1185
    :cond_6
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_7

    if-eqz v1, :cond_7

    .line 1186
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v11, v1}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1189
    :cond_7
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    sub-long/2addr v4, v6

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    goto/16 :goto_0

    :cond_8
    move-object v1, v0

    goto/16 :goto_1
.end method

#Method_MessageReceiver
.method public static a(Lcom/tencent/mobileqq/app/QQAppInterface;Lcom/tencent/mobileqq/data/MessageRecord;Z)Z
    .locals 13

    .prologue
    const/4 v3, 0x0

    const/4 v12, 0x2

    const/4 v2, 0x1

    .line 1203
    if-eqz p1, :cond_0

    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    if-nez v0, :cond_2

    iget-object v0, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgData:[B

    if-nez v0, :cond_2

    .line 1204
    :cond_0
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_1

    .line 1205
    const-string v0, "Q.msg.MessageHandlerUtils"

    const-string v1, "---------------msgFilter message [before filter] is null !"

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    :cond_1
    move v0, v2

    .line 1315
    :goto_0
    return v0

    .line 1210
    :cond_2
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v4

    .line 1212
    const/4 v0, 0x0

    .line 1213
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v1

    if-eqz v1, :cond_13

    .line 1214
    new-instance v0, Ljava/lang/StringBuilder;

    const/16 v1, 0x100

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(I)V

    .line 1215
    const-string v1, "---------------msgFilter istroop: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " uid: "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-wide v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgUid:J

    invoke-virtual {v1, v6, v7}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " shmsgseq: "

    .line 1216
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-wide v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->shmsgseq:J

    invoke-virtual {v1, v6, v7}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " friendUin: "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " senderUin: "

    .line 1217
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-object v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->senderuin:Ljava/lang/String;

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " msgType: "

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " time:"

    .line 1218
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    iget-wide v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->time:J

    invoke-virtual {v1, v6, v7}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " msgContent: "

    .line 1219
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {p1}, Lcom/tencent/mobileqq/data/MessageRecord;->getLogColorContent()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v6, " isNormalMsg: "

    .line 1220
    invoke-virtual {v1, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1, p2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-object v1, v0

    .line 1223
    :goto_1
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lcom/tencent/mobileqq/app/message/QQMessageFacade;

    move-result-object v0

    iget-object v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->frienduin:Ljava/lang/String;

    iget v7, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-virtual {v0, v6, v7}, Lcom/tencent/mobileqq/app/message/QQMessageFacade;->b(Ljava/lang/String;I)Ljava/util/List;

    move-result-object v0

    .line 1228
    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    if-eq v6, v2, :cond_3

    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v7, 0x402

    if-eq v6, v7, :cond_3

    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v7, 0xbb8

    if-ne v6, v7, :cond_7

    .line 1231
    :cond_3
    if-eqz v0, :cond_6

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_6

    .line 1232
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v6

    :cond_4
    invoke-interface {v6}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_6

    invoke-interface {v6}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1233
    invoke-static {v0, p1, v3, p2}, Lakpx;->a(Lcom/tencent/mobileqq/data/MessageRecord;Lcom/tencent/mobileqq/data/MessageRecord;ZZ)Z

    move-result v0

    if-eqz v0, :cond_4

    .line 1234
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_5

    if-eqz v1, :cond_5

    .line 1235
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, " filterType: troop msg isNormalMsg="

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, p2}, Ljava/lang/StringBuilder;->append(Z)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1236
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    .line 1240
    :cond_5
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    sub-long v4, v6, v4

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v0, v2

    .line 1241
    goto/16 :goto_0

    .line 1246
    :cond_6
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lawzy;

    move-result-object v0

    invoke-virtual {v0, p1}, Lawzy;->f(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-eqz v0, :cond_11

    move v0, v2

    .line 1247
    goto/16 :goto_0

    .line 1254
    :cond_7
    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-static {v6}, Lakpx;->c(I)Z

    move-result v6

    if-eqz v6, :cond_b

    .line 1255
    if-eqz v0, :cond_a

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_a

    .line 1256
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v6

    :cond_8
    invoke-interface {v6}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_a

    invoke-interface {v6}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1257
    invoke-static {v0, p1, p2}, Lakpx;->a(Lcom/tencent/mobileqq/data/MessageRecord;Lcom/tencent/mobileqq/data/MessageRecord;Z)Z

    move-result v0

    if-eqz v0, :cond_8

    .line 1258
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_9

    if-eqz v1, :cond_9

    .line 1259
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, " filterType: "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    iget v3, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1260
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    .line 1264
    :cond_9
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    sub-long v4, v6, v4

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v0, v2

    .line 1265
    goto/16 :goto_0

    .line 1270
    :cond_a
    invoke-virtual {p0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lawzy;

    move-result-object v0

    invoke-virtual {v0, p1}, Lawzy;->f(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-eqz v0, :cond_11

    move v0, v2

    .line 1271
    goto/16 :goto_0

    .line 1274
    :cond_b
    iget v6, p1, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v7, 0x1c34

    if-ne v6, v7, :cond_e

    .line 1275
    if-eqz v0, :cond_11

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_11

    .line 1276
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v6

    :cond_c
    invoke-interface {v6}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_11

    invoke-interface {v6}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1277
    invoke-static {v0, p1, v2}, Lakpx;->a(Lcom/tencent/mobileqq/data/MessageRecord;Lcom/tencent/mobileqq/data/MessageRecord;Z)Z

    move-result v0

    if-eqz v0, :cond_c

    .line 1278
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_d

    if-eqz v1, :cond_d

    .line 1279
    const-string v0, " filterType: other"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1280
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    .line 1287
    :cond_d
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    sub-long v4, v6, v4

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v0, v2

    .line 1288
    goto/16 :goto_0

    .line 1294
    :cond_e
    if-eqz v0, :cond_11

    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v6

    if-lez v6, :cond_11

    .line 1295
    invoke-interface {v0}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v6

    :cond_f
    invoke-interface {v6}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_11

    invoke-interface {v6}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 1296
    iget-wide v8, v0, Lcom/tencent/mobileqq/data/MessageRecord;->time:J

    iget-wide v10, p1, Lcom/tencent/mobileqq/data/MessageRecord;->time:J

    cmp-long v7, v8, v10

    if-nez v7, :cond_f

    iget-object v0, v0, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    iget-object v7, p1, Lcom/tencent/mobileqq/data/MessageRecord;->msg:Ljava/lang/String;

    invoke-virtual {v0, v7}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_f

    .line 1297
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_10

    if-eqz v1, :cond_10

    .line 1298
    const-string v0, " filterType: other"

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1299
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->w(Ljava/lang/String;ILjava/lang/String;)V

    .line 1303
    :cond_10
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    sub-long v4, v6, v4

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v0, v2

    .line 1304
    goto/16 :goto_0

    .line 1309
    :cond_11
    invoke-static {}, Lcom/tencent/qphone/base/util/QLog;->isColorLevel()Z

    move-result v0

    if-eqz v0, :cond_12

    if-eqz v1, :cond_12

    .line 1310
    const-string v0, "Q.msg.MessageHandlerUtils"

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v12, v1}, Lcom/tencent/qphone/base/util/QLog;->d(Ljava/lang/String;ILjava/lang/String;)V

    .line 1314
    :cond_12
    invoke-static {}, Lbfnl;->a()Lbfnl;

    move-result-object v0

    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v6

    sub-long v4, v6, v4

    invoke-virtual {v0, v4, v5}, Lbfnl;->h(J)V

    move v0, v3

    .line 1315
    goto/16 :goto_0

    :cond_13
    move-object v1, v0

    goto/16 :goto_1
.end method

.method public static a(Lcom/tencent/mobileqq/data/MessageRecord;)Z
    .locals 4

    .prologue
    const/4 v1, 0x0

    .line 1459
    invoke-static {}, Lbjal;->a()Lcom/tencent/common/app/AppInterface;

    move-result-object v0

    .line 1460
    instance-of v2, v0, Lcom/tencent/mobileqq/app/QQAppInterface;

    if-eqz v2, :cond_1

    .line 1461
    iget v2, p0, Lcom/tencent/mobileqq/data/MessageRecord;->extraflag:I

    const v3, 0x8000

    if-eq v2, v3, :cond_0

    check-cast v0, Lcom/tencent/mobileqq/app/QQAppInterface;

    .line 1462
    invoke-virtual {v0}, Lcom/tencent/mobileqq/app/QQAppInterface;->a()Lawzy;

    move-result-object v0

    invoke-virtual {v0, p0}, Lawzy;->b(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    if-nez v0, :cond_0

    const/4 v0, 0x1

    .line 1464
    :goto_0
    return v0

    :cond_0
    move v0, v1

    .line 1462
    goto :goto_0

    :cond_1
    move v0, v1

    .line 1464
    goto :goto_0
.end method

.method public static a(Ljava/util/ArrayList;)Z
    .locals 4
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/ArrayList",
            "<",
            "Lcom/tencent/mobileqq/data/MessageRecord;",
            ">;)Z"
        }
    .end annotation

    .prologue
    .line 155
    const/4 v1, 0x0

    .line 156
    if-nez p0, :cond_0

    .line 165
    :goto_0
    return v1

    .line 159
    :cond_0
    invoke-virtual {p0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v2

    :cond_1
    invoke-interface {v2}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_2

    invoke-interface {v2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 160
    invoke-virtual {v0}, Lcom/tencent/mobileqq/data/MessageRecord;->isSend()Z

    move-result v3

    if-nez v3, :cond_1

    iget-boolean v0, v0, Lcom/tencent/mobileqq/data/MessageRecord;->isread:Z

    if-nez v0, :cond_1

    .line 161
    const/4 v0, 0x1

    :goto_1
    move v1, v0

    .line 165
    goto :goto_0

    :cond_2
    move v0, v1

    goto :goto_1
.end method

.method public static a(Lcom/tencent/qphone/base/remote/ToServiceMsg;Lcom/tencent/qphone/base/remote/FromServiceMsg;)[Ljava/lang/String;
    .locals 22

    .prologue
    .line 1380
    if-eqz p0, :cond_0

    if-nez p1, :cond_1

    .line 1382
    :cond_0
    const/4 v2, 0x0

    .line 1451
    :goto_0
    return-object v2

    .line 1385
    :cond_1
    const/4 v2, 0x7

    new-array v6, v2, [Ljava/lang/String;

    .line 1387
    move-object/from16 v0, p0

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/ToServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "startTime"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v14

    .line 1388
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_app2msf_atAppSite"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v12

    .line 1389
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_app2msf_atMsfSite"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v10

    .line 1390
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_msf2net_atMsfSite"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v16

    .line 1392
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_net2msf_atMsfSite"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v8

    .line 1393
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_msf2app_atMsfSite"

    const-wide/16 v4, 0x0

    invoke-virtual {v2, v3, v4, v5}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v4

    .line 1394
    move-object/from16 v0, p1

    iget-object v2, v0, Lcom/tencent/qphone/base/remote/FromServiceMsg;->extraData:Landroid/os/Bundle;

    const-string/jumbo v3, "timestamp_msf2app_atAppSite"

    const-wide/16 v18, 0x0

    move-wide/from16 v0, v18

    invoke-virtual {v2, v3, v0, v1}, Landroid/os/Bundle;->getLong(Ljava/lang/String;J)J

    move-result-wide v2

    .line 1395
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v18

    .line 1397
    const/4 v7, 0x1

    sub-long v20, v18, v14

    invoke-static/range {v20 .. v21}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v20

    aput-object v20, v6, v7

    .line 1398
    const/4 v7, 0x2

    sub-long v20, v8, v16

    invoke-static/range {v20 .. v21}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v20

    aput-object v20, v6, v7

    .line 1399
    const/4 v7, 0x3

    sub-long v20, v16, v14

    invoke-static/range {v20 .. v21}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v20

    aput-object v20, v6, v7

    .line 1400
    const/4 v7, 0x4

    sub-long v20, v18, v8

    invoke-static/range {v20 .. v21}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v20

    aput-object v20, v6, v7

    .line 1403
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    .line 1404
    const-string v20, "handler"

    move-object/from16 v0, v20

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1405
    const-wide/16 v20, 0x0

    cmp-long v20, v12, v20

    if-eqz v20, :cond_6

    .line 1407
    const-string/jumbo v20, "|"

    move-object/from16 v0, v20

    invoke-virtual {v7, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v20

    sub-long v14, v12, v14

    invoke-static {v14, v15}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v14

    move-object/from16 v0, v20

    invoke-virtual {v0, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v14

    const-string/jumbo v15, "|app"

    invoke-virtual {v14, v15}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1410
    :goto_1
    const-wide/16 v14, 0x0

    cmp-long v14, v10, v14

    if-eqz v14, :cond_5

    .line 1412
    const-string/jumbo v14, "|"

    invoke-virtual {v7, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v14

    sub-long v12, v10, v12

    invoke-static {v12, v13}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v14, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    const-string/jumbo v13, "|msf"

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1415
    :goto_2
    const-wide/16 v12, 0x0

    cmp-long v12, v16, v12

    if-eqz v12, :cond_2

    .line 1417
    const-string/jumbo v12, "|"

    invoke-virtual {v7, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    sub-long v10, v16, v10

    invoke-static {v10, v11}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v12, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    const-string/jumbo v11, "|net"

    invoke-virtual {v10, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1420
    :cond_2
    const/4 v10, 0x5

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    aput-object v7, v6, v10

    .line 1424
    new-instance v7, Ljava/lang/StringBuilder;

    invoke-direct {v7}, Ljava/lang/StringBuilder;-><init>()V

    .line 1425
    const-string v10, "net"

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1426
    const-wide/16 v10, 0x0

    cmp-long v10, v4, v10

    if-eqz v10, :cond_4

    .line 1428
    const-string/jumbo v10, "|"

    invoke-virtual {v7, v10}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v10

    sub-long v8, v4, v8

    invoke-static {v8, v9}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v10, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string/jumbo v9, "|msf"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1431
    :goto_3
    const-wide/16 v8, 0x0

    cmp-long v8, v2, v8

    if-eqz v8, :cond_3

    .line 1433
    const-string/jumbo v8, "|"

    invoke-virtual {v7, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    sub-long v4, v2, v4

    invoke-static {v4, v5}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v8, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    const-string/jumbo v5, "|app"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1436
    :goto_4
    const-string/jumbo v4, "|"

    invoke-virtual {v7, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    sub-long v2, v18, v2

    invoke-static {v2, v3}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v4, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v2

    const-string/jumbo v3, "|handler"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1437
    const/4 v2, 0x6

    invoke-virtual {v7}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    aput-object v3, v6, v2

    .line 1440
    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    .line 1441
    const-string/jumbo v3, "{"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1442
    const-string/jumbo v3, "total:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x1

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1443
    const-string v3, "net:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x2

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1444
    const-string/jumbo v3, "send:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x3

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1445
    const-string/jumbo v3, "recv:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x4

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1446
    const-string/jumbo v3, "sendDetail:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x5

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const-string v4, ","

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1447
    const-string/jumbo v3, "recvDetail:"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v3

    const/4 v4, 0x6

    aget-object v4, v6, v4

    invoke-virtual {v3, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1448
    const-string/jumbo v3, "}"

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 1449
    const/4 v3, 0x0

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    aput-object v2, v6, v3

    move-object v2, v6

    .line 1451
    goto/16 :goto_0

    :cond_3
    move-wide v2, v4

    goto/16 :goto_4

    :cond_4
    move-wide v4, v8

    goto/16 :goto_3

    :cond_5
    move-wide v10, v12

    goto/16 :goto_2

    :cond_6
    move-wide v12, v14

    goto/16 :goto_1
.end method

.method public static b(Lcom/tencent/mobileqq/data/MessageRecord;)Z
    .locals 3

    .prologue
    const/4 v0, 0x1

    .line 1468
    iget v1, p0, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    if-eq v1, v0, :cond_1

    iget v1, p0, Lcom/tencent/mobileqq/data/MessageRecord;->istroop:I

    const/16 v2, 0xbb8

    if-eq v1, v2, :cond_1

    .line 1470
    invoke-static {p0}, Lakas;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v0

    .line 1472
    :cond_0
    :goto_0
    return v0

    :cond_1
    invoke-static {p0}, Lakas;->a(Lcom/tencent/mobileqq/data/MessageRecord;)Z

    move-result v1

    if-eqz v1, :cond_2

    iget v1, p0, Lcom/tencent/mobileqq/data/MessageRecord;->extraflag:I

    if-eqz v1, :cond_0

    :cond_2
    const/4 v0, 0x0

    goto :goto_0
.end method

.method public static b(Ljava/util/ArrayList;)Z
    .locals 6
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/ArrayList",
            "<",
            "Lcom/tencent/mobileqq/data/MessageRecord;",
            ">;)Z"
        }
    .end annotation

    .prologue
    const/4 v1, 0x0

    .line 169
    if-nez p0, :cond_0

    .line 179
    :goto_0
    return v1

    .line 172
    :cond_0
    const/4 v2, 0x1

    .line 173
    invoke-virtual {p0}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v3

    :cond_1
    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v0

    if-eqz v0, :cond_3

    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/tencent/mobileqq/data/MessageRecord;

    .line 174
    iget v4, v0, Lcom/tencent/mobileqq/data/MessageRecord;->msgtype:I

    const/16 v5, -0x7d6

    if-ne v4, v5, :cond_2

    instance-of v0, v0, Lcom/tencent/mobileqq/data/MessageForFoldMsg;

    if-nez v0, :cond_1

    :cond_2
    move v0, v1

    :goto_1
    move v1, v0

    .line 179
    goto :goto_0

    :cond_3
    move v0, v2

    goto :goto_1
.end method
