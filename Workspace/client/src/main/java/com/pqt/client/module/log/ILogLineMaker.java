package com.pqt.client.module.log;

import com.pqt.core.entities.log.LogLine;
import com.pqt.core.entities.log.ILoggable;

public interface ILogLineMaker {
	LogLine make(ILoggable loggable);
}
