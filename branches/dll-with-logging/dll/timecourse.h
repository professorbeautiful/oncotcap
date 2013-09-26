#define MAXTCOURSE 256

struct TIMECOURSE {
	double t;
	double pr;
	double logs;
	double avgcells;
	double avggtzero;
} tcoursedata[MAXTCOURSE];

int ntcoursedata;

char strTimecourse[65536];