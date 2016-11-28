CREATE TABLE Users (
uname CHAR(20),
phone CHAR(10),
email CHAR(20),
password CHAR(20),
PRIMARY KEY (email)
);

CREATE TABLE Manager (
userid CHAR(20),
PRIMARY KEY (userid),
FOREIGN KEY (userid) REFERENCES Users(email)
);


CREATE TABLE Topic (
topic CHAR(10),
PRIMARY KEY(topic)
);

CREATE TABLE Messages (
mid INTEGER UNIQUE,
data CHAR(1400),
timestamp DATE,
sender CHAR(20),
PRIMARY KEY (mid,sender),
FOREIGN KEY (sender) REFERENCES Users(email)
);

CREATE TABLE Is_friends (
user1id CHAR(20),
user2id CHAR(20),
PRIMARY KEY (user1id,user2id),
FOREIGN KEY (user1id) REFERENCES Users(email) ON DELETE CASCADE,
FOREIGN KEY (user2id) REFERENCES Users(email) ON DELETE CASCADE
);



CREATE TABLE Topic_Message (
topic CHAR(10),
messageid INTEGER,
PRIMARY KEY (topic,messageid),
FOREIGN KEY (messageid) REFERENCES Messages(mid) ON DELETE CASCADE,
FOREIGN KEY (topic) REFERENCES Topic(topic) ON DELETE CASCADE
);

CREATE TABLE Topic_User (
topic CHAR(10),
userid CHAR(20),
PRIMARY KEY (topic,userid),
FOREIGN KEY (userid) REFERENCES Users(email) ON DELETE CASCADE,
FOREIGN KEY (topic) REFERENCES Topic(topic) ON DELETE CASCADE
);


CREATE TABLE Circle (
userid CHAR(20),
messageid INTEGER,
pflag INT,
PRIMARY KEY (userid,messageid),
FOREIGN KEY (userid) REFERENCES Users(email) ON DELETE CASCADE,
FOREIGN KEY (messageid) REFERENCES Messages(mid) ON DELETE CASCADE
);

CREATE TABLE Private_Messages (
messageid INTEGER,
owner CHAR(20),
PRIMARY KEY (messageid,owner),
FOREIGN KEY (messageid) REFERENCES Messages(mid),
FOREIGN KEY (owner) REFERENCES Users(email) ON DELETE CASCADE
);

CREATE TABLE Group_Owner (
gname CHAR(20),
duration INTEGER DEFAULT '7',
userid CHAR(20) NOT NULL,
PRIMARY KEY (gname),
FOREIGN KEY (userid) REFERENCES Users(email)
);

CREATE TABLE Member_Of (
gname CHAR(20),
userid CHAR(20),
PRIMARY KEY (gname,userid),
FOREIGN KEY (gname) REFERENCES Group_Owner(gname) ON DELETE CASCADE,
FOREIGN KEY (userid) REFERENCES Users(email) ON DELETE CASCADE
);

CREATE TABLE Group_Messages (
group_name CHAR(20),
messageid INTEGER,
since DATE,
PRIMARY KEY (group_name,messageid),
FOREIGN KEY (group_name) REFERENCES Group_Owner(gname) ON DELETE CASCADE,
FOREIGN KEY (messageid) REFERENCES Messages(mid) ON DELETE CASCADE
);