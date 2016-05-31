package com.example.instation;

import org.litepal.crud.DataSupport;

public class InstationJsonUser extends DataSupport {
		private int id;
		//名字
		private String name;
		//ID号
		private String watchID;
		//账户名
		private String account;
		//权限。。。licence
		private String licence;
		
		public String getLicence() {
			return licence;
		}
		public void setLicence(String licence) {
			this.licence = licence;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getWatchID() {
			return watchID;
		}
		public void setWatchID(String watchID) {
			this.watchID = watchID;
		}
		@Override
		public String toString() {
			return "JsonUser [id=" + id +", licence=" + licence +", account=" + account + ", name=" + name + ", watchID="
					+ watchID + "]";
		}
		
	}