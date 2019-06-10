package com.stars.tv.bean;

import java.io.Serializable;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.05.30
 */
public class LiveTvEpgBean implements Serializable {

        String startTime;
        String programName;

        @Override
        public String toString() {
            return "Epg{" +
                    "startTime='" + startTime + '\'' +
                    ", programName='" + programName + '\'' +
                    '}';
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }
}
