package com.m7.abs.common.constant.common;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * 时间单位
 *
 * @author death00
 * @date 2019/9/24 18:39
 */
@Getter
public enum DisScheduleUnit {

	// 毫秒
	MILLISECONDS(TimeUnit.MILLISECONDS),

	// 秒
	SECONDS(TimeUnit.SECONDS),

	// 分钟
	MINUTES(TimeUnit.MINUTES);

	private TimeUnit unit;

	private DisScheduleUnit(TimeUnit unit) {
		Preconditions.checkNotNull(unit);
		this.unit = unit;
	}
}
