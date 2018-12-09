package com.sixtofly.entity;

import java.util.List;

public class WuTong {
        private List<Data> Data;
        private boolean Status;
        private String ResultValue;
        private String StatusCode;
        private String StatusMessage;
        private int RecordCount;
        public void setData(List<Data> Data) {
            this.Data = Data;
        }
        public List<Data> getData() {
            return Data;
        }

        public void setStatus(boolean Status) {
            this.Status = Status;
        }
        public boolean getStatus() {
            return Status;
        }

        public void setResultValue(String ResultValue) {
            this.ResultValue = ResultValue;
        }
        public String getResultValue() {
            return ResultValue;
        }

        public void setStatusCode(String StatusCode) {
            this.StatusCode = StatusCode;
        }
        public String getStatusCode() {
            return StatusCode;
        }

        public void setStatusMessage(String StatusMessage) {
            this.StatusMessage = StatusMessage;
        }
        public String getStatusMessage() {
            return StatusMessage;
        }

        public void setRecordCount(int RecordCount) {
            this.RecordCount = RecordCount;
        }
        public int getRecordCount() {
            return RecordCount;
        }
}
