export type Company={id:number;companyName:string};
export type Application={id:number;companyId:number;companyName:string;jobRole:string;jobLocation:string;status:string};
export type HRContact={id:number;companyId:number;companyName:string;name:string;email?:string;phone?:string;designation?:string};
export type Interview={id:number;applicationId:number;companyName:string;jobRole:string;roundNumber:number;roundName:string;interviewType:string;scheduledAt?:string;status:string;result:string;notes?:string};
export type Offer={id:number;applicationId:number;companyName:string;jobRole:string;revisionNumber:number;offeredCtc:number;fixedCtc?:number;variableCtc?:number;joiningBonus?:number;offerDate:string;validityDate?:string;status:string;notes?:string};
export type FollowUp={id:number;applicationId:number;companyName:string;jobRole:string;hrContactId?:number;followUpDate:string;followUpType:string;description:string;status:string;notes?:string};
export type Activity={id:number;applicationId:number;companyName?:string;jobRole?:string;activityType:string;title:string;description?:string;activityDate:string;systemGenerated?:boolean};
