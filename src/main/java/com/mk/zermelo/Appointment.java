package com.mk.zermelo;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
		entity = User.class,
		parentColumns = "id",
		childColumns = "userId"
		),
		indices = {@Index("id"), @Index("userId")}
)
public class Appointment {

	// TODO refactor to list
	public Appointment(int id, long start, long end, int startTimeSlot, int endTimeSlot, String startTimeSlotName,
	                   String endTimeSlotName, boolean valid, boolean cancelled, boolean modified, boolean moved,
	                   boolean hidden, boolean isnew, String remark, String changeDescription, String branch,
	                   String branchOfSchool, long created, long lastModified, int appointmentInstance, String type,
	                   String subjects, String teachers, String groupsInDepartments, String groups,
	                   String locationsOfBranch, String locations, int userId) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.startTimeSlot = startTimeSlot;
		this.endTimeSlot = endTimeSlot;
		this.startTimeSlotName = startTimeSlotName;
		this.endTimeSlotName = endTimeSlotName;
		this.valid = valid;
		this.cancelled = cancelled;
		this.modified = modified;
		this.moved = moved;
		this.hidden = hidden;
		this.isnew = isnew;
		this.remark = remark;
		this.changeDescription = changeDescription;
		this.branch = branch;
		this.branchOfSchool = branchOfSchool;
		this.created = created;
		this.lastModified = lastModified;
		this.appointmentInstance = appointmentInstance;
		this.type = type;
		this.subjects = subjects;
		this.teachers = teachers;
		this.groupsInDepartments = groupsInDepartments;
		this.groups = groups;
		this.locationsOfBranch = locationsOfBranch;
		this.locations = locations;

		this.userId = userId;
	}

	@PrimaryKey(autoGenerate = true)
	private int id;
	private long start;
	private long end;
	private int startTimeSlot;
	private int endTimeSlot;
	private String startTimeSlotName;
	private String endTimeSlotName;
	private boolean valid;
	private boolean cancelled;
	private boolean modified;
	private boolean moved;
	private boolean hidden;
	// TODO fix this perhaps
	private boolean isnew;
	private String remark;
	private String changeDescription;
	private String branch;
	private String branchOfSchool;
	private long created;
	private long lastModified;
	private int appointmentInstance;
	private String type;
	private String subjects;
	private String teachers;
	private String groupsInDepartments;
	private String groups;
	private String locationsOfBranch;
	private String locations;

	private int userId;

	/*
	"id":519369,
	"start":1543306800,
	"end":1543309800,
	"startTimeSlot":2,
	"endTimeSlot":2,
	"startTimeSlotName":"u2",
	"endTimeSlotName":"u2",
	"valid":true,
	"cancelled":false,
	"modified":false,
	"moved":false,
	"hidden":false,
	"new":false,
	"remark":"",
	"changeDescription":"",
	"branch":"breda",
	"branchOfSchool":202,
	"created":1542286709,
	"lastModified":1542286709,
	"appointmentInstance":355391,
	"type":"lesson",
	"subjects":["netl"],
	"teachers":["kzp"],
	"groupsInDepartments":[6860],
	"groups":["v6.netl3"],
	"locationsOfBranch":[372],
	"locations":["b204"]
	*/

	public int getId() {
		return id;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public int getStartTimeSlot() {
		return startTimeSlot;
	}

	public int getEndTimeSlot() {
		return endTimeSlot;
	}

	public String getStartTimeSlotName() {
		return startTimeSlotName;
	}

	public String getEndTimeSlotName() {
		return endTimeSlotName;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isModified() {
		return modified;
	}

	public boolean isMoved() {
		return moved;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isIsnew() {
		return isnew;
	}

	public String getRemark() {
		return remark;
	}

	public String getChangeDescription() {
		return changeDescription;
	}

	public String getBranch() {
		return branch;
	}

	public String getBranchOfSchool() {
		return branchOfSchool;
	}

	public long getCreated() {
		return created;
	}

	public long getLastModified() {
		return lastModified;
	}

	public int getAppointmentInstance() {
		return appointmentInstance;
	}

	public String getType() {
		return type;
	}

	public String getSubjects() {
		return subjects;
	}

	public String getTeachers() {
		return teachers;
	}

	public String getGroupsInDepartments() {
		return groupsInDepartments;
	}

	public String getGroups() {
		return groups;
	}

	public String getLocationsOfBranch() {
		return locationsOfBranch;
	}

	public String getLocations() {
		return locations;
	}

	public int getUserId() {
		return userId;
	}
}
