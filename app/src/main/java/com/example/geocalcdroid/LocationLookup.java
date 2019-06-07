package com.example.geocalcdroid;

import org.joda.time.DateTime;
import org.parceler.Parcel;
@Parcel
public class LocationLookup {
        double origLat;
        double origLng;
        double endLat;
        double endLng;

        public DateTime getTimeStamp() {
                return timeStamp;
        }

        public void setTimeStamp(DateTime timeStamp) {
                this.timeStamp = timeStamp;
        }

        DateTime timeStamp;
        String _key;

        public double getOrigLat() {
                return origLat;
        }

        public void setOrigLat(double origLat) {
                this.origLat = origLat;
        }

        public double getOrigLng() {
                return origLng;
        }

        public void setOrigLng(double origLng) {
                this.origLng = origLng;
        }

        public double getEndLat() {
                return endLat;
        }

        public void setEndLat(double endLat) {
                this.endLat = endLat;
        }

        public double getEndLng() {
                return endLng;
        }

        public void setEndLng(double endLng) {
                this.endLng = endLng;
        }

        public String get_key() {
                return _key;
        }

        public void set_key(String _key) {
                this._key = _key;
        }
}
