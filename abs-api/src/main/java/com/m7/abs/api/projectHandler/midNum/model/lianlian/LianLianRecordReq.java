package com.m7.abs.api.projectHandler.midNum.model.lianlian;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LianLianRecordReq {
    private String bind_id;
    private String recorder_id;
    private String caller;
    private String called;
    private String record_file_url;
    private String call_result;
    private String extend;
    private String caller_show;
    private String called_show;
    private String bill_duration;
    private String begin_time;
    private String release_time;
    private String connect_time;
    private String alerting_time;
    private String caller_area;
    private String called_area;
}
