drop table Customer cascade constraints;
drop table CustomerDetails cascade constraints;
drop table DependentHas;
drop table NonMember;
drop table HotelMember;
drop table Booking cascade constraints;
-- drop table CustomerMakesBooking;
-- drop table CustomerUpdatesBooking;
drop table CustomerBooking cascade constraints;
drop table Account cascade constraints;
drop table Room cascade constraints;
drop table Hotel cascade constraints;
drop table HotelHasBooking;
drop table Employee cascade constraints;
drop table Manager;
drop table Cleaner;
drop table Cleans;
drop table Payment cascade constraints;
drop table BasicCost  cascade constraints;
drop table Checks;


CREATE TABLE Hotel (
	HotelID int,
	HotelName varchar(80),
	PRIMARY KEY (HotelID)
);
grant select on Hotel to public;


CREATE TABLE BasicCost(
    RoomCost Float,
    Points int,
    PRIMARY KEY (RoomCost)
);
grant select on BasicCost to public;

CREATE TABLE Payment(
	PaymentID int,
	RoomCost Float,
	AdditionalCosts Float,
	PRIMARY KEY (PaymentID),
	FOREIGN KEY (RoomCost) REFERENCES BasicCost /*ON UPDATE CASCADE*/);
				/*ON DELETE NO ACTION*/
grant select on Payment to public;


CREATE TABLE CustomerDetails (
	Email varchar(80),
	Cus_Name varchar(80),
	Age int,
	Address varchar(80),
	PRIMARY KEY (Email)
);
grant select on CustomerDetails to public;

CREATE TABLE Booking (
	BookingID int,
	StartDate Date NOT NULL,
	EndDate Date NOT NULL,
	SizeOfParty int NOT NULL,
	PRIMARY KEY (BookingID)
);
grant select on Booking to public;

CREATE TABLE Account(
	Username varchar(80),
	Password varchar(80) NOT NULL,
	CreditCard varchar(16) NOT NULL,
	PRIMARY KEY (Username)
	/*FOREIGN KEY (CreditCard) REFERENCES Customer ON UPDATE CASCADE*/);
			    /*ON DELETE NO ACTION*/
grant select on Account to public;


CREATE TABLE Customer (
	CreditCard varchar(16),
    Email  varchar(80),
 	Account varchar(15) Not NULL UNIQUE,
    PaymentID int UNIQUE,
    PRIMARY KEY (CreditCard),
    FOREIGN KEY (Email) REFERENCES CustomerDetails ON DELETE CASCADE /*ON UPDATE CASCADE*/,
    FOREIGN KEY (Account) REFERENCES Account ON DELETE CASCADE /*ON UPDATE CASCADE*/,
    FOREIGN KEY (PaymentID) REFERENCES Payment /*ON UPDATE CASCADE*/
);
grant select on Customer to public;


CREATE TABLE DependentHas (
	Dep_Name varchar(80),
	Age int,
	CreditCard varchar(16),
	PRIMARY KEY (CreditCard, Dep_Name),
	FOREIGN KEY (CreditCard) REFERENCES Customer ON DELETE CASCADE /*ON UPDATE CASCADE*/
);
grant select on DependentHas to public;


CREATE TABLE NonMember (
	CreditCard varchar(16),
	PRIMARY KEY (CreditCard),
	FOREIGN KEY (CreditCard) REFERENCES Customer /*ON UPDATE CASCADE*/
);
grant select on NonMember to public;


CREATE TABLE HotelMember (
	CreditCard varchar(16),
	Points int,
	PRIMARY KEY (CreditCard),
	FOREIGN KEY (CreditCard) REFERENCES Customer /*ON UPDATE CASCADE*/
);
grant select on HotelMember to public;


CREATE TABLE CustomerBooking(
	CreditCard varchar(16),
	BookingID int,
	EndDate varchar(80),
	PRIMARY KEY (CreditCard, BookingID),
	FOREIGN KEY (CreditCard) REFERENCES Customer ON DELETE CASCADE /*ON UPDATE CASCADE*/,
	FOREIGN KEY (BookingID) REFERENCES Booking ON DELETE CASCADE /*ON UPDATE CASCADE*/
);
grant select on CustomerBooking to public;


-- CREATE TABLE CustomerUpdatesBooking(
-- 	CreditCard int,
-- 	BookingID int,
-- 	EndDate varchar(80),
-- 	PRIMARY KEY (CreditCard, BookingID),
-- 	FOREIGN KEY (CreditCard) REFERENCES Customer ON DELETE CASCADE /*ON UPDATE CASCADE*/,
-- 	FOREIGN KEY (BookingID) REFERENCES Booking ON DELETE CASCADE /*ON UPDATE CASCADE*/
-- );
-- grant select on CustomerUpdatesBooking to public;


CREATE TABLE Room(
	Room_Number int,
	Room_Date varchar(80),
	Rate int NOT NULL,
	BookingID int,
	PRIMARY KEY (Room_Number),
	FOREIGN KEY (BookingID) REFERENCES Booking ON DELETE CASCADE /*ON UPDATE CASCADE*/
);
grant select on Room to public;


CREATE TABLE HotelHasBooking (
	HotelID int,
	BookingID int,
	PRIMARY KEY (HotelID, BookingID),
	FOREIGN KEY (HotelID) REFERENCES Hotel ON DELETE CASCADE /*ON UPDATE CASCADE*/,
    FOREIGN KEY (BookingID) REFERENCES Booking ON DELETE CASCADE /*ON UPDATE CASCADE*/
);
grant select on HotelHasBooking to public;


CREATE TABLE Employee (
	EmployeeID int,
	Emp_Name varchar(80),
	HotelID int NOT NULL,
	PRIMARY KEY (EmployeeID),
	FOREIGN KEY (HotelID) REFERENCES Hotel
);
grant select on Employee to public;


CREATE TABLE Manager (
	EmployeeID int,
	Mng_Name varchar(80),
	HotelID int NOT NULL,
	PRIMARY KEY (EmployeeID),
	FOREIGN KEY (EmployeeID) REFERENCES Employee /*ON UPDATE CASCADE*/ ON DELETE CASCADE,
	FOREIGN KEY (HotelID) REFERENCES Hotel
);
grant select on Manager to public;


CREATE TABLE Cleaner (
	EmployeeID int,
	Cln_Name varchar(80),
	HotelID int NOT NULL,
	PRIMARY KEY (EmployeeID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee /*ON UPDATE CASCADE*/ ON DELETE CASCADE,
	FOREIGN KEY (HotelID) REFERENCES Hotel
);
grant select on Cleaner to public;


CREATE TABLE Cleans (
	EmployeeID int,
	Room_Number int,
	PRIMARY KEY (EmployeeID, Room_Number),
	FOREIGN KEY (EmployeeID) REFERENCES Employee ON DELETE CASCADE /*ON UPDATE CASCADE*/,
	FOREIGN KEY (Room_Number) REFERENCES Room ON DELETE CASCADE /*ON UPDATE CASCADE*/
);
grant select on Cleans to public;


CREATE TABLE Checks (
	PaymentID int,
	EmployeeID int,
	PRIMARY KEY (PaymentID, EmployeeID),
	FOREIGN KEY (PaymentID) REFERENCES Payment ON DELETE CASCADE /*ON UPDATE CASCADE*/,
	FOREIGN KEY (EmployeeID) REFERENCES Employee /*ON UPDATE CASCADE*/
);
grant select on Checks to public;

insert into Hotel
values (1001, 'Best Western');

insert into Hotel
values (1002, 'Holiday Inn');

insert into Hotel
values (1003, 'Marriott');

insert into Hotel
values (1004, 'Trump International Hotel');

insert into Hotel
values (1005, 'Hyatt');


insert into BasicCost
values (123, 246);

insert into BasicCost
values (10000, 20000);

insert into BasicCost
values (200, 400);

insert into BasicCost
values (500, 1000);

insert into BasicCost
values (299, 598);


insert into Payment
values (100, 123, 0);

insert into Payment
values (102, 10000, 20);

insert into Payment
values (103, 200, 100);

insert into Payment
values (104, 500, 0);

insert into Payment
values (105, 299, 50);

insert into Payment
values (106, 123, 0);

insert into Payment
values (107, 10000, 20);

insert into Payment
values (108, 200, 100);

insert into Payment
values (109, 500, 0);

insert into Payment
values (110, 299, 50);

insert into Payment
values (111, 299, 50);


insert into CustomerDetails
values ('dave@gmail.com', 'Dave David', 18, '1234 Some Street, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('joe@gmail.com', 'Joe Joey', 20, '5678 Another Street, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('bob@gmail.com', 'Bob Bobby', 21, '123 Some Street Way, Toronto, O.N, Canada');

insert into CustomerDetails
values ('john@gmail.com', 'John Jonny', 40, '456 Crescent Drive, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('tim@gmail.com', 'Tim Timmy', 20, '1235 Some Street, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('sam@gmail.com', 'Sam Sammy', 24, '493 Road Street, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('sean@gmail.com', 'Sean Shawn', 23, '1236 Some Avenue, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('todd@gmail.com', 'Todd Toddy', 88, '1237 Some Avenue, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('emily@gmail.com', 'Emily Dickonson', 43, '1235 Some Avenue, Vancouver, B.C, Canada');

insert into CustomerDetails
values ('kings@gmail.com', 'King Kinlgy II', 70, '4321 Some Avenue, Vancouver, B.C, Canada');


insert into Booking
values (101, to_date('2020-06-01', 'YYYY-MM-DD'), to_date('2020-07-17', 'YYYY-MM-DD'), 2);

insert into Booking
values (201, to_date('2020-01-01', 'YYYY-MM-DD'), to_date('2020-01-02', 'YYYY-MM-DD'), 2);

insert into Booking
values (322, to_date('2020-02-02', 'YYYY-MM-DD'), to_date('2020-02-10', 'YYYY-MM-DD'), 2);

insert into Booking
values (412, to_date('2020-08-10', 'YYYY-MM-DD'), to_date('2020-08-11', 'YYYY-MM-DD'), 3);

insert into Booking
values (533, to_date('2020-10-30', 'YYYY-MM-DD'), to_date('2020-11-01', 'YYYY-MM-DD'), 1);


insert into Account
values ('dave123', 'password123', '1234567891113434');

insert into Account
values ('joe123', '123abcdef', '4100563478943662');

insert into Account
values ('bob123', 'abc123456789', '7564300216559058');

insert into Account
values ('john123', 'john123!', '5829212029440912');

insert into Account
values ('tim123', '!@#$%^^&', '4747927400119283');

insert into Account
values ('sam123', 'sam123', '9824829899091942');

insert into Account
values ('sean123', 'sean123', '1212444566840922');

insert into Account
values ('todd123', 'todd123', '1018123819231234');

insert into Account
values ('emily123', 'emily123', '1957471130481273');

insert into Account
values ('kings123', 'kings123', '7678476320841003');



insert into Customer
values ('1234567891113434', 'dave@gmail.com', 'dave123', 100);

insert into Customer
values ('4100563478943662', 'joe@gmail.com', 'joe123', 102);

insert into Customer
values ('7564300216559058',  'bob@gmail.com', 'bob123', 103);

insert into Customer
values ('5829212029440912', 'john@gmail.com', 'john123', 104);

insert into Customer
values ('4747927400119283', 'tim@gmail.com', 'tim123', 105);

insert into Customer
values ('9824829899091942', 'sam@gmail.com', 'sam123', 106);

insert into Customer
values ('1212444566840922', 'sean@gmail.com', 'sean123', 107);

insert into Customer
values ('1018123819231234', 'todd@gmail.com', 'todd123', 109);

insert into Customer
values ('1957471130481273', 'emily@gmail.com', 'emily123', 111);

insert into Customer
values ('7678476320841003', 'kings@gmail.com', 'kings123', 110);


insert into DependentHas(CreditCard, Dep_Name, Age)
values ('1234567891113434', 'Don David', 15);

insert into DependentHas(CreditCard, Dep_Name, Age)
values ('1234567891113434', 'Jim David', 10);

insert into DependentHas(CreditCard, Dep_Name, Age)
values ('7564300216559058', 'Brad Bobby', 9);

insert into DependentHas(CreditCard, Dep_Name, Age)
values ('5829212029440912', 'Jonie Jonny', 17);

insert into DependentHas(CreditCard, Dep_Name, Age)
values ('4747927400119283', 'Tom Timmy', 2);


insert into NonMember
values ('1234567891113434');

insert into NonMember
values ('4100563478943662');

insert into NonMember
values ('7564300216559058');

insert into NonMember
values ('5829212029440912');

insert into NonMember
values ('4747927400119283');


insert into HotelMember
values ('9824829899091942', 11111111111);

insert into HotelMember
values ('1212444566840922', 0);

insert into HotelMember
values ('1018123819231234', 2222232);

insert into HotelMember
values ('1957471130481273', 11);

insert into HotelMember
values ('7678476320841003', 223234);


insert into CustomerBooking
values ('1234567891113434', 412, to_date('2020-08-11', 'YYYY-MM-DD'));

insert into CustomerBooking
values ('4100563478943662', 101, to_date('2020-07-17', 'YYYY-MM-DD'));

insert into CustomerBooking
values ('7564300216559058', 322, to_date('2020-02-10', 'YYYY-MM-DD'));

insert into CustomerBooking
values ('5829212029440912', 201, to_date('2020-01-02', 'YYYY-MM-DD'));

insert into CustomerBooking
values ('4100563478943662', 533, to_date('2020-11-01', 'YYYY-MM-DD'));


insert into Room
values (101, '2020-06-01 2020-07-17', 123, 412);

insert into Room
values (103, '2020-06-01 2020-07-17', 10000, 101);

insert into Room
values (331, '2020-02-02 2020-02-10', 200, 322);

insert into Room
values (505, '2020-01-01 2020-01-02', 500, 201);

insert into Room
values (110, '2020-10-30 2020-11-01', 299, 533);


insert into HotelHasBooking
values (1001, 412);

insert into HotelHasBooking
values (1001, 101);

insert into HotelHasBooking
values (1001, 322);

insert into HotelHasBooking
values (1001, 201);

insert into HotelHasBooking
values (1001, 533);


insert into Employee
values (100, 'Bruce Wayne', 1001);

insert into Employee
values (101, 'Tim Drake', 1002);

insert into Employee
values (102, 'Jack Black', 1003);

insert into Employee
values (103, 'Dick Grayson', 1004);

insert into Employee
values (104, 'Clark Kent', 1005);

insert into Employee
values (105, 'Alice Goldberg', 1001);

insert into Employee
values (106, 'Harry Potter', 1002);

insert into Employee
values (107, 'Ron Weasley', 1003);

insert into Employee
values (108, 'Lucias Fox', 1004);

insert into Employee
values (109, 'Jose Ramerez', 1005);


insert into Manager
values (100, 'Bruce Wayne', 1001);

insert into Manager
values (101, 'Tim Drake', 1002);

insert into Manager
values (102, 'Jack Black', 1003);

insert into Manager
values (103, 'Dick Grayson', 1004);

insert into Manager
values (104, 'Clark Kent', 1005);


insert into Cleaner
values (105, 'Alice Goldberg', 1001);

insert into Cleaner
values (106, 'Harry Potter', 1002);

insert into Cleaner
values (107, 'Ron Weasley', 1003);

insert into Cleaner
values (108, 'Lucias Fox', 1004);

insert into Cleaner
values (109, 'Jose Ramerez', 1005);


insert into Cleans
values (105, 101);

insert into Cleans
values (105, 103);

insert into Cleans
values (105, 331);

insert into Cleans
values (105, 505);

insert into Cleans
values (105, 110);


insert into Checks
values (100, 105);

insert into Checks
values (102, 105);

insert into Checks
values (103, 105);

insert into Checks
values (104, 105);

insert into Checks
values (105, 105);
