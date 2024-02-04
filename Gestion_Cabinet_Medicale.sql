/*Drop Table Medecin;*/
Create Table Medecin (ID_Med  NUMBER(10)  not null primary key,Ueser_Name Varchar2(60)unique NOT NULL,password varchar2(60) NOT NULL, Nom varchar2(50) not null ,Prenom varchar2(50) not null,DateN date ,sex varchar2(10),ADR varchar2(500),ADRmail varchar2(50),Numtel varchar2(10),Specilité varchar2(500));

/*Drop SEQUENCE ID_Med;*/
CREATE SEQUENCE ID_Med START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;

insert INTO Medecin VALUES (ID_Med.nextval,'med_01','123','Rebai','Med Younes','01-01-2001','Homme','Cité 40 logement zeralda','younes741rebai@gmail.com','0561334141','Cardiology'); /*utilisateur par defaut*/



      
/*Drop Table Secretaire;*/
Create Table Secretaire (ID_sec NUMBER(10)  not null primary key,Ueser_Name Varchar2(30)unique NOT NULL,password varchar2(30) NOT NULL, Nom varchar2(50) not null ,Prenom varchar2(50) not null,DateN date ,sex varchar2(10),ADR varchar2(500),ADRmail varchar2(50),Numtel varchar2(10));

/*Drop SEQUENCE ID_sec;*/
CREATE SEQUENCE ID_sec START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;

insert INTO Secretaire VALUES (id_sec.nextval,'sec_01','123','Bessai','Med Younes','02/02/2001','Homme','Cité 40 logement ben aknouna','BessaiZoheir@gmail.com','0561334142');



/*Drop Table Patient;*/   
Create Table Patient (ID_Patient DECIMAL(12) primary key, Name varchar2(50)  NOT NULL,FirstName varchar2(50)  NOT NULL,BirthDate date ,sex varchar2(10),ADR varchar2(300),ADRmail varchar2(50),PhoneNum Decimal(10),Prof varchar2(200),DateEng date);

/*Drop SEQUENCE ID_Patient;*/
create sequence ID_Patient START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;



/*Drop Table RDV; */  
Create Table RDV (NUM_RDV DECIMAL(12) primary key, Date_RDV  Date  NOT NULL,H_RDV varchar2(5) NOT NULL ,ID_Med NUMBER(10)references Medecin (ID_Med)Not null,Doctor_Name varchar2(50) NOT NULL,SpE varchar2(500) ,ID_Pat NUMBER(10)references Patient (ID_Patient) not null,Patient_Name varchar2(100),Commentary varchar2(500));

/*Drop SEQUENCE NUM_RDV;*/
create sequence NUM_RDV START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;


/*Drop Table Medoc; */  
Create Table Medoc (ID_Medoc DECIMAL(12) primary key,Nom Varchar2(200) NOT NULL,Famille varchar2(60) NOT NULL ,Forme varchar2(60) NOT NULL,Obs varchar2(200));

/*Drop SEQUENCE ID_Medoc;*/
CREATE SEQUENCE ID_Medoc START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;



/*Drop Table Consult;*/ 
Create Table Consult (ID_Cons varchar2(40) primary key,Date_consult  Date  NOT NULL,ID_Pat NUMBER(10)references Patient (ID_Patient) not null,NUM_RDV DECIMAL(12)references RDV (NUM_RDV) not null,Patient_Name varchar2(30),GRP_SNG varchar2(25) NOT NULL ,taille varchar2(25) NOT NULL,poids varchar2(20),tension varchar2(25),Diab_O_N varchar2(3),T_Diab varchar2(25),Diag varchar2(2000),M_Diag varchar2(1000),Obs varchar2(2000),MP varchar2(25),Bilan_O_N varchar2(3));                                                                                    

/*drop SEQUENCE Num_SEQ_Consult;*/
CREATE SEQUENCE Num_SEQ_Consult START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;


/*SELECT Num_SEQ_Consult.nextval T FROM dual ;*/



 /*Drop Table Bilan;*/ 
 Create Table Bilan (ID_Bilan DECIMAL(12) primary key,NUM_RDV DECIMAL(12)references RDV (NUM_RDV) not null,Date_ajout  Date  NOT NULL,analyses_dem VARCHAR2(1000),resultat VARCHAR2(2000),Comm VARCHAR2(2000));

/*drop SEQUENCE Num_SEQ_Bilan;*/
CREATE SEQUENCE Num_SEQ_Bilan START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;


 /*Drop Table paiement;*/ 
 Create Table paiement (Num_Paiement DECIMAL(12) primary key,ID_Cons varchar2(40) references Consult (ID_Cons) not null,ID_Pat NUMBER(10)references Patient (ID_Patient) not null,Date_paiement  Date  NOT NULL,Date_Consult  Date  NOT NULL,Commentaire VARCHAR2(1000),prix_a_payer DECIMAL(25),prix_payé DECIMAL(25),etat varchar2(50));

/*drop SEQUENCE Num_Paiement;*/
CREATE SEQUENCE Num_Paiement START WITH  1 INCREMENT BY   1 NOCACHE NOCYCLE;

