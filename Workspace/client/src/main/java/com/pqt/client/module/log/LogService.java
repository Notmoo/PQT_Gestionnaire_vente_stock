package com.pqt.client.module.log;

import com.pqt.core.entities.log.ILoggable;
import com.pqt.core.entities.log.LogLine;

import java.util.List;

//TODO Issue #6 : écrire contenu méthodes
//TODO Issue #5 : écrire javadoc
public class LogService {

	public void log(ILoggable loggable) {

	}

	public List<LogLine> getAllLogs() {
		return null;
	}

	public List<LogLine> getLastLogs(int number) {
		return null;
	}

	public List<LogLine> getLastLogs(int number, int offset) {
		return null;
	}

}
