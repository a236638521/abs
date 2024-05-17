/*
 Navicat Premium Data Transfer

 Source Server         : V7_Beta
 Source Server Type    : PostgreSQL
 Source Server Version : 120005
 Source Host           : 172.31.15.12:5432
 Source Catalog        : abs_business
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120005
 File Encoding         : 65001

 Date: 08/12/2022 11:00:05
*/


-- ----------------------------
-- Table structure for dispatcher_fail_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."dispatcher_fail_log";
CREATE TABLE "public"."dispatcher_fail_log" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "project_code" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "data" text COLLATE "pg_catalog"."default",
  "method" varchar(50) COLLATE "pg_catalog"."default",
  "content_type" varchar(255) COLLATE "pg_catalog"."default",
  "retry_times" int4 DEFAULT 0,
  "max_times" int4 NOT NULL DEFAULT 5,
  "result" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6) NOT NULL,
  "last_update_time" timestamptz(6),
  "deleted" int4 NOT NULL DEFAULT 0,
  "next_execute_time" timestamptz(6),
  "interval_time" int8,
  "request_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."dispatcher_fail_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."dispatcher_fail_log"."id" IS 'ID主键';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."project_code" IS '项目代码';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."url" IS 'url';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."data" IS '数据';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."method" IS '请求方式;POST;GET';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."content_type" IS 'content_type;application/json;charset=UTF-8';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."retry_times" IS '重试次数';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."max_times" IS '最大次数';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."result" IS '重试结果';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."last_update_time" IS '更新时间';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."deleted" IS '逻辑删除;0:未删除 1:已删除';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."next_execute_time" IS '下次执行时间';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."interval_time" IS '执行间隔时间：单位/秒';
COMMENT ON COLUMN "public"."dispatcher_fail_log"."request_id" IS 'request id';
COMMENT ON TABLE "public"."dispatcher_fail_log" IS '推送失败记录表';

-- ----------------------------
-- Table structure for flash_sm_delivery_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."flash_sm_delivery_log";
CREATE TABLE "public"."flash_sm_delivery_log" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamptz(6) NOT NULL,
  "channel_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "task_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "target" varchar(50) COLLATE "pg_catalog"."default",
  "sender" varchar(50) COLLATE "pg_catalog"."default",
  "status" varchar(50) COLLATE "pg_catalog"."default",
  "update_time" timestamptz(6),
  "result" varchar(50) COLLATE "pg_catalog"."default",
  "result_msg" varchar(250) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."flash_sm_delivery_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."id" IS '绑定ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."channel_id" IS '通道ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."content" IS '发送内容';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."task_id" IS '任务ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."target" IS '发送目标';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."sender" IS '发送者';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."status" IS '送达情况  1:短信发送成功   2:发送失败';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."result" IS '发送结果';
COMMENT ON COLUMN "public"."flash_sm_delivery_log"."result_msg" IS '发送结果内容';

-- ----------------------------
-- Table structure for flash_sm_delivery_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."flash_sm_delivery_task";
CREATE TABLE "public"."flash_sm_delivery_task" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_task_id" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6) NOT NULL,
  "channel_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_url" varchar(255) COLLATE "pg_catalog"."default",
  "template_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "cdr_push_task_id" varchar(64) COLLATE "pg_catalog"."default",
  "receive_notify_time" timestamptz(6),
  "status" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."flash_sm_delivery_task" OWNER TO "postgres";
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."id" IS '绑定ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."channel_task_id" IS '关联ID;和其他通道关联的ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."channel_id" IS '通道ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."notify_url" IS '回执推送地址';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."template_id" IS '模板ID';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."cdr_push_task_id" IS 'cdr数据推送taskId';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."receive_notify_time" IS '回执接收时间';
COMMENT ON COLUMN "public"."flash_sm_delivery_task"."status" IS '状态：等待回执，发送成功，发送失败';

-- ----------------------------
-- Table structure for middle_number_bind
-- ----------------------------
DROP TABLE IF EXISTS "public"."middle_number_bind";
CREATE TABLE "public"."middle_number_bind" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "association_id" varchar(255) COLLATE "pg_catalog"."default",
  "tel_x" varchar(255) COLLATE "pg_catalog"."default",
  "tel_a" varchar(255) COLLATE "pg_catalog"."default",
  "tel_b" varchar(255) COLLATE "pg_catalog"."default",
  "expiration" int8,
  "call_recording" int4,
  "status" int4
)
;
ALTER TABLE "public"."middle_number_bind" OWNER TO "postgres";
COMMENT ON COLUMN "public"."middle_number_bind"."id" IS '绑定ID';
COMMENT ON COLUMN "public"."middle_number_bind"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."middle_number_bind"."association_id" IS '关联ID;和其他通道关联的ID';
COMMENT ON COLUMN "public"."middle_number_bind"."tel_x" IS '中间号';
COMMENT ON COLUMN "public"."middle_number_bind"."tel_a" IS 'A号码';
COMMENT ON COLUMN "public"."middle_number_bind"."tel_b" IS 'B号码';
COMMENT ON COLUMN "public"."middle_number_bind"."expiration" IS '绑定过期时间;单位/秒';
COMMENT ON COLUMN "public"."middle_number_bind"."call_recording" IS '是否录音;1:是;0:否,默认否';
COMMENT ON COLUMN "public"."middle_number_bind"."status" IS '状态;0:初始 1:启用 2:禁用';
COMMENT ON TABLE "public"."middle_number_bind" IS '小号绑定记录表';

-- ----------------------------
-- Table structure for middle_number_bind_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."middle_number_bind_log";
CREATE TABLE "public"."middle_number_bind_log" (
  "id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "association_id" varchar(255) COLLATE "pg_catalog"."default",
  "tel_x" varchar(255) COLLATE "pg_catalog"."default",
  "tel_a" varchar(255) COLLATE "pg_catalog"."default",
  "tel_b" varchar(255) COLLATE "pg_catalog"."default",
  "expiration" int8,
  "call_recording" bool,
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "user_data" varchar(255) COLLATE "pg_catalog"."default",
  "ic_display_flag" int2,
  "bind_type" varchar(8) COLLATE "pg_catalog"."default",
  "channel_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamptz(6) NOT NULL,
  "record_receive_url" varchar(255) COLLATE "pg_catalog"."default",
  "channel_code" varchar(255) COLLATE "pg_catalog"."default",
  "last_update_time" timestamptz(6),
  "expire_time" timestamptz(6),
  "group_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."middle_number_bind_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."middle_number_bind_log"."id" IS '绑定ID';
COMMENT ON COLUMN "public"."middle_number_bind_log"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."middle_number_bind_log"."association_id" IS '关联ID;和其他通道关联的ID';
COMMENT ON COLUMN "public"."middle_number_bind_log"."tel_x" IS '中间号';
COMMENT ON COLUMN "public"."middle_number_bind_log"."tel_a" IS 'A号码';
COMMENT ON COLUMN "public"."middle_number_bind_log"."tel_b" IS 'B号码';
COMMENT ON COLUMN "public"."middle_number_bind_log"."expiration" IS '绑定过期时间;单位/秒';
COMMENT ON COLUMN "public"."middle_number_bind_log"."call_recording" IS '是否录音;1:是;0:否,默认否';
COMMENT ON COLUMN "public"."middle_number_bind_log"."status" IS '状态;0:初始 1:启用 2:禁用';
COMMENT ON COLUMN "public"."middle_number_bind_log"."user_data" IS '用户自定义字段';
COMMENT ON COLUMN "public"."middle_number_bind_log"."ic_display_flag" IS '是否显示中间号码；0-显示真实号码；1-显示中间号码';
COMMENT ON COLUMN "public"."middle_number_bind_log"."bind_type" IS '0-AXB;1-AX;2-AXYB';
COMMENT ON COLUMN "public"."middle_number_bind_log"."channel_id" IS '通道ID';
COMMENT ON COLUMN "public"."middle_number_bind_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."middle_number_bind_log"."record_receive_url" IS '话单推送地址';
COMMENT ON COLUMN "public"."middle_number_bind_log"."channel_code" IS '通道编码';
COMMENT ON COLUMN "public"."middle_number_bind_log"."last_update_time" IS '/*最后一次更新时间*/';
COMMENT ON COLUMN "public"."middle_number_bind_log"."group_id" IS '分组ID';
COMMENT ON TABLE "public"."middle_number_bind_log" IS '小号绑定记录表';

-- ----------------------------
-- Table structure for middle_number_bind_log_backup
-- ----------------------------
DROP TABLE IF EXISTS "public"."middle_number_bind_log_backup";
CREATE TABLE "public"."middle_number_bind_log_backup" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "association_id" varchar(255) COLLATE "pg_catalog"."default",
  "tel_x" varchar(255) COLLATE "pg_catalog"."default",
  "tel_a" varchar(255) COLLATE "pg_catalog"."default",
  "tel_b" varchar(255) COLLATE "pg_catalog"."default",
  "expiration" int8,
  "call_recording" bool,
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "user_data" varchar(255) COLLATE "pg_catalog"."default",
  "ic_display_flag" int2,
  "bind_type" varchar(8) COLLATE "pg_catalog"."default",
  "channel_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamptz(6) NOT NULL,
  "record_receive_url" varchar(255) COLLATE "pg_catalog"."default",
  "channel_code" varchar(255) COLLATE "pg_catalog"."default",
  "last_update_time" timestamptz(6),
  "backup_time" timestamptz(6)
)
;
ALTER TABLE "public"."middle_number_bind_log_backup" OWNER TO "postgres";
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."id" IS '绑定ID';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."association_id" IS '关联ID;和其他通道关联的ID';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."tel_x" IS '中间号';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."tel_a" IS 'A号码';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."tel_b" IS 'B号码';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."expiration" IS '绑定过期时间;单位/秒';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."call_recording" IS '是否录音;1:是;0:否,默认否';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."status" IS '状态;0:初始 1:启用 2:禁用';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."user_data" IS '用户自定义字段';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."ic_display_flag" IS '是否显示中间号码；0-显示真实号码；1-显示中间号码';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."bind_type" IS '0-AXB;1-AX;2-AXYB';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."channel_id" IS '通道ID';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."record_receive_url" IS '话单推送地址';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."channel_code" IS '通道编码';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."last_update_time" IS '/*最后一次更新时间*/';
COMMENT ON COLUMN "public"."middle_number_bind_log_backup"."backup_time" IS '备份时间';
COMMENT ON TABLE "public"."middle_number_bind_log_backup" IS '小号绑定记录表';

-- ----------------------------
-- Table structure for middle_number_cdr
-- ----------------------------
DROP TABLE IF EXISTS "public"."middle_number_cdr";
CREATE TABLE "public"."middle_number_cdr" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "mapping_id" varchar(255) COLLATE "pg_catalog"."default",
  "tel_x" varchar(255) COLLATE "pg_catalog"."default",
  "caller" varchar(255) COLLATE "pg_catalog"."default",
  "callee" varchar(255) COLLATE "pg_catalog"."default",
  "call_recording" bool,
  "create_time" timestamptz(6) NOT NULL,
  "result" varchar(255) COLLATE "pg_catalog"."default",
  "call_display" varchar(255) COLLATE "pg_catalog"."default",
  "caller_show" varchar(255) COLLATE "pg_catalog"."default",
  "called_show" varchar(255) COLLATE "pg_catalog"."default",
  "caller_area" varchar(255) COLLATE "pg_catalog"."default",
  "called_area" varchar(255) COLLATE "pg_catalog"."default",
  "bill_duration" int8,
  "begin_time" timestamptz(6),
  "alerting_time" timestamptz(6),
  "connect_time" timestamptz(6),
  "release_time" timestamptz(6),
  "oss_task_id" varchar(255) COLLATE "pg_catalog"."default",
  "cdr_push_task_id" varchar(255) COLLATE "pg_catalog"."default",
  "record_file_proxy" varchar(255) COLLATE "pg_catalog"."default",
  "record_file_host" varchar(255) COLLATE "pg_catalog"."default",
  "record_file_path" varchar(255) COLLATE "pg_catalog"."default",
  "channel_bind_id" varchar(255) COLLATE "pg_catalog"."default",
  "channel_record_id" varchar(255) COLLATE "pg_catalog"."default",
  "release_dir" varchar(100) COLLATE "pg_catalog"."default",
  "channel_group_id" varchar(255) COLLATE "pg_catalog"."default",
  "group_id" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."middle_number_cdr" OWNER TO "postgres";
COMMENT ON COLUMN "public"."middle_number_cdr"."id" IS '通话记录ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."mapping_id" IS '系统绑定记录ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."tel_x" IS '中间号';
COMMENT ON COLUMN "public"."middle_number_cdr"."caller" IS '主叫号码';
COMMENT ON COLUMN "public"."middle_number_cdr"."callee" IS '被叫号码';
COMMENT ON COLUMN "public"."middle_number_cdr"."call_recording" IS '是否录音;t:是;f:否,默认否';
COMMENT ON COLUMN "public"."middle_number_cdr"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."middle_number_cdr"."caller_show" IS '主叫端外显';
COMMENT ON COLUMN "public"."middle_number_cdr"."called_show" IS '被叫端外显';
COMMENT ON COLUMN "public"."middle_number_cdr"."caller_area" IS '主叫号码归属地';
COMMENT ON COLUMN "public"."middle_number_cdr"."called_area" IS '被叫号码归属地';
COMMENT ON COLUMN "public"."middle_number_cdr"."bill_duration" IS '通话时长';
COMMENT ON COLUMN "public"."middle_number_cdr"."begin_time" IS '开始时间';
COMMENT ON COLUMN "public"."middle_number_cdr"."alerting_time" IS '振铃时间';
COMMENT ON COLUMN "public"."middle_number_cdr"."connect_time" IS '接听时间';
COMMENT ON COLUMN "public"."middle_number_cdr"."release_time" IS '挂机时间';
COMMENT ON COLUMN "public"."middle_number_cdr"."oss_task_id" IS 'oss转存taskId';
COMMENT ON COLUMN "public"."middle_number_cdr"."cdr_push_task_id" IS 'cdr数据推送taskId';
COMMENT ON COLUMN "public"."middle_number_cdr"."channel_bind_id" IS '第三方通道绑定记录ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."channel_record_id" IS '第三方通话记录ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."release_dir" IS '挂机方';
COMMENT ON COLUMN "public"."middle_number_cdr"."channel_group_id" IS '第三方通话记录ID';
COMMENT ON COLUMN "public"."middle_number_cdr"."group_id" IS '分组ID';

-- ----------------------------
-- Table structure for middle_number_sms_cdr
-- ----------------------------
DROP TABLE IF EXISTS "public"."middle_number_sms_cdr";
CREATE TABLE "public"."middle_number_sms_cdr" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "mapping_id" varchar(255) COLLATE "pg_catalog"."default",
  "tel_x" varchar(255) COLLATE "pg_catalog"."default",
  "caller" varchar(255) COLLATE "pg_catalog"."default",
  "callee" varchar(255) COLLATE "pg_catalog"."default",
  "channel_bind_id" varchar(255) COLLATE "pg_catalog"."default",
  "channel_record_id" varchar(255) COLLATE "pg_catalog"."default",
  "result" varchar(255) COLLATE "pg_catalog"."default",
  "sms_number" int4,
  "sms_time" timestamptz(6),
  "cdr_push_task_id" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6) NOT NULL
)
;
ALTER TABLE "public"."middle_number_sms_cdr" OWNER TO "postgres";
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."id" IS '通话记录ID';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."mapping_id" IS '系统绑定记录ID';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."tel_x" IS '中间号';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."caller" IS '主叫号码';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."callee" IS '被叫号码';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."channel_bind_id" IS '第三方通道绑定记录ID';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."channel_record_id" IS '第三方通话记录ID';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."result" IS '短信发送结果';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."sms_number" IS '短信发送条数';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."sms_time" IS '短信发送时间';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."cdr_push_task_id" IS 'cdr数据推送taskId';
COMMENT ON COLUMN "public"."middle_number_sms_cdr"."create_time" IS '创建时间';

-- ----------------------------
-- Table structure for oss_upload_fail_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."oss_upload_fail_log";
CREATE TABLE "public"."oss_upload_fail_log" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "account_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "project_code" varchar(255) COLLATE "pg_catalog"."default",
  "file_url" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_suffix" varchar(255) COLLATE "pg_catalog"."default",
  "oss_key" varchar(255) COLLATE "pg_catalog"."default",
  "retry_times" int4 DEFAULT 0,
  "max_times" int4 DEFAULT 5,
  "result" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6) NOT NULL,
  "last_update_time" timestamptz(6),
  "deleted" int4 NOT NULL DEFAULT 0,
  "next_execute_time" timestamptz(6),
  "interval_time" int8,
  "request_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."oss_upload_fail_log" OWNER TO "postgres";
COMMENT ON COLUMN "public"."oss_upload_fail_log"."id" IS 'ID主键';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."account_id" IS '账户ID';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."project_code" IS '项目代码';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."file_url" IS '文件下载地址';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."file_name" IS 'oss文件上传后名字';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."file_suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."oss_key" IS 'oss upload key';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."retry_times" IS '重试次数';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."max_times" IS '最大尝试次数';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."result" IS '重试结果';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."last_update_time" IS '更新时间';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."deleted" IS '逻辑删除;0:未删除 1:已删除';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."next_execute_time" IS '下次执行时间';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."interval_time" IS '执行间隔时间：单位/秒';
COMMENT ON COLUMN "public"."oss_upload_fail_log"."request_id" IS 'request id';
COMMENT ON TABLE "public"."oss_upload_fail_log" IS 'OSS文件上传失败记录';

-- ----------------------------
-- Function structure for get_rand_datetime
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."get_rand_datetime"("start_date" date, "end_date" date);
CREATE OR REPLACE FUNCTION "public"."get_rand_datetime"("start_date" date, "end_date" date)
  RETURNS "pg_catalog"."timestamp" AS $BODY$  
        DECLARE   
                interval_days integer;
                random_seconds integer; 
                random_dates integer;
                random_date date; 
                random_time time;
 BEGIN
                interval_days := end_date - start_date;
                random_dates:= trunc(random()*interval_days);
                random_date := start_date + random_dates;
                random_seconds:= trunc(random()*3600*24);
                random_time:=' 00:00:00'::time+(random_seconds || ' second')::INTERVAL;
                RETURN random_date +random_time;
 END; 
        $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "public"."get_rand_datetime"("start_date" date, "end_date" date) OWNER TO "postgres";

-- ----------------------------
-- Function structure for get_tel
-- ----------------------------
DROP FUNCTION IF EXISTS "public"."get_tel"();
CREATE OR REPLACE FUNCTION "public"."get_tel"()
  RETURNS "pg_catalog"."varchar" AS $BODY$ DECLARE
startLength INT DEFAULT 11;
endLength INT DEFAULT 11;
first_no VARCHAR ( 100 ) DEFAULT '1';
chars_str VARCHAR ( 100 ) DEFAULT '0123456789';
return_str VARCHAR ( 300 ) DEFAULT SUBSTRING ( '3578', CAST ( ( 1 + random( ) * 3 ) AS INT ), 1 );
i INT;
end1 INT;
BEGIN
		end1 := CAST ( ( random( ) * ( endLength - startLength ) ) AS INT ) + startLength;
	FOR i IN 1..end1 - 2
	loop
	return_str = concat ( return_str, SUBSTRING ( chars_str, CAST ( ( 1 + random( ) * 9 ) AS INT ), 1 ) );
	
END loop;
RETURN concat ( first_no, return_str );

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION "public"."get_tel"() OWNER TO "postgres";

-- ----------------------------
-- Primary Key structure for table dispatcher_fail_log
-- ----------------------------
ALTER TABLE "public"."dispatcher_fail_log" ADD CONSTRAINT "dispatcher_fail_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table flash_sm_delivery_log
-- ----------------------------
CREATE INDEX "flash_sms_task_id_index" ON "public"."flash_sm_delivery_log" USING btree (
  "task_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table flash_sm_delivery_log
-- ----------------------------
ALTER TABLE "public"."flash_sm_delivery_log" ADD CONSTRAINT "flash_sm_delivery_task_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table flash_sm_delivery_task
-- ----------------------------
ALTER TABLE "public"."flash_sm_delivery_task" ADD CONSTRAINT "flash_sm_delivery_log_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table middle_number_bind
-- ----------------------------
ALTER TABLE "public"."middle_number_bind" ADD CONSTRAINT "middle_number_bind_log_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table middle_number_bind_log
-- ----------------------------
CREATE INDEX "bind_log_association_id_index" ON "public"."middle_number_bind_log" USING btree (
  "association_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "bind_log_create_time_index" ON "public"."middle_number_bind_log" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
);
CREATE INDEX "bind_log_tel_a_and_tel_b_index" ON "public"."middle_number_bind_log" USING btree (
  "tel_a" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tel_b" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "bind_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table middle_number_bind_log
-- ----------------------------
ALTER TABLE "public"."middle_number_bind_log" ADD CONSTRAINT "middle_number_bind_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table middle_number_bind_log_backup
-- ----------------------------
ALTER TABLE "public"."middle_number_bind_log_backup" ADD CONSTRAINT "middle_number_bind_log_copy1_pkey1" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table middle_number_cdr
-- ----------------------------
CREATE INDEX "cdr_begin_time_index" ON "public"."middle_number_cdr" USING btree (
  "begin_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
);
CREATE INDEX "cdr_channel_record_id_index" ON "public"."middle_number_cdr" USING btree (
  "channel_record_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table middle_number_cdr
-- ----------------------------
ALTER TABLE "public"."middle_number_cdr" ADD CONSTRAINT "middle_number_cdr_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table middle_number_sms_cdr
-- ----------------------------
ALTER TABLE "public"."middle_number_sms_cdr" ADD CONSTRAINT "middle_number_sms_cdr_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table oss_upload_fail_log
-- ----------------------------
ALTER TABLE "public"."oss_upload_fail_log" ADD CONSTRAINT "oss_upload_fail_log_pkey" PRIMARY KEY ("id");
