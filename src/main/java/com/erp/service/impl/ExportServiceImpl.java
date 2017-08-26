package com.erp.service.impl;

import com.erp.mapper.ExpertMapper;
import com.erp.mapper.VisaMapper;
import com.erp.model.*;
import com.erp.service.*;
import com.erp.utils.ExportUtil;
import com.erp.utils.ListToMap;
import com.mysql.jdbc.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Sumif;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.validator.internal.util.Contracts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Value("${admin.sale.column}")
    private String adminColumn;

    @Autowired
    private ExpertMapper expertMapper;
    @Autowired
    private VisaService visaService;

    @Autowired
    private StudentInfoService studentInfoService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private TransferCaseService transferCaseService;
    @Autowired
    private PlanService planService;
    @Autowired
    private SupplementService supplementService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private  StudentCopywritingService copywritingService;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private  ExperienceService experienceService;

    @Autowired
    private  ExamService examService;

    @Autowired
    private AfterBusinessService afterBusinessService;

    @Autowired
    private  OperatorService operatorService;

    @Autowired
    private  RefundService refundService;

    @Autowired
    private VisitService visitService;
    @Autowired
    private OfferService offerService;

    @Autowired
    private BonusService bonusService;

    @Autowired
    private MemberApplyService memberApplyService;

    @Override
    public List<Expert> queryList() {
        List<Expert> experts=new ArrayList<>();
        Expert expert= expertMapper.selectByPrimaryKey(41);
        experts.add(expert);
        return experts;
    }

    @Override
    public List<ExportParam> export( MemberApply memberApply) {
        //查询申请信息
        List<Apply> applyList=getApply(memberApply);

        List<ExportParam> exports=new ArrayList<>();
        for(Apply apply:applyList){
            ExportParam exportParam=new ExportParam();

            //Reply reply=setOfferReply(apply.getId());
            Supplement supplement=getSupplement(apply.getId());


            if(apply.getApplyType()==1){
                exportParam.setMainApply(apply);
                exportParam.setMainSupplement(supplement);
                DateParam replyResult=setParamsReply(apply.getId());
                exportParam.setMainReplyDate(replyResult);
                String visitRecord= getVisit(apply.getId(),2);
                exportParam.setMainVisit(visitRecord);
                Offer offer=getOffer(apply.getId(),1);
                exportParam.setMainOffer(offer);
                //奖金
                Bonus applybonus= getBonus(apply.getId(),1);
                exportParam.setApplyBonus(applybonus);
                Bonus visabonus= getBonus(apply.getId(),2);
                exportParam.setVisaBonus(visabonus);
                TransferCase mainCase=new TransferCase();
                mainCase.setSaleToCopyDate(getCaseDate(apply.getId(),1,1));
                mainCase.setCopyToConnectDate(getCaseDate(apply.getId(),2,1));
                mainCase.setConnectToCopyDate(getCaseDate(apply.getId(),3,1));
                exportParam.setMainCase(mainCase);
                //跟催
                String visitUnCondition= getVisit(apply.getId(),1);
                exportParam.setUnConditionVisit(visitUnCondition);
            }else if(apply.getApplyType()==2){
                exportParam.setLangApply(apply);
                exportParam.setLangSupplement(supplement);
                DateParam replyResult=setParamsReply(apply.getId());
                exportParam.setLangStayReplyDate(replyResult);
                String visitRecord= getVisit(apply.getId(),2);
                exportParam.setLangVisit(visitRecord);
                Offer offer=getOffer(apply.getId(),2);
                exportParam.setLangOffer(offer);
                //奖金
                Bonus applybonus= getBonus(apply.getId(),1);
                exportParam.setApplyBonus(applybonus);
                Bonus visabonus= getBonus(apply.getId(),2);
                exportParam.setVisaBonus(visabonus);
                TransferCase langCase=new TransferCase();
                langCase.setSaleToCopyDate(getCaseDate(apply.getId(),1,2));
                langCase.setCopyToConnectDate(getCaseDate(apply.getId(),2,2));
                langCase.setConnectToCopyDate(getCaseDate(apply.getId(),3,2));
                exportParam.setLangCase(langCase);
                exportParam.setMainCase(langCase);
            }
            if(apply.getApplyType()==1 && apply.getRelationStatus()==1){
                Apply langApply= getStayInfo(apply.getId(),2);
               if(langApply.getId()!=null){
                   DateParam replyResult=setParamsReply(langApply.getId());
                   exportParam.setLangReplyDate(replyResult);
               }
                exportParam.setLangApply(langApply);
                exportParam.setLangSupplement(supplement);
                Offer offer=getOffer(langApply.getId(),2);
                exportParam.setLangOffer(offer);


                //主课住宿
                Apply mainStayApply=getStayInfo(apply.getId(),3);
                if(mainStayApply.getId()!=null){
                    exportParam.setMainStayApply(mainStayApply);
                    Reply mainStayReply=setOfferReply(mainStayApply.getId());
                    if(mainStayReply.getId()!=null){
                        DateParam replyResult=setParamsReply(mainStayApply.getId());
                        exportParam.setMainStayReplyDate(replyResult);
                    }
                    String visitRecord= getVisit(mainStayApply.getId(),2);
                    exportParam.setMainStayVisit(visitRecord);
                    Offer mainStayOffer=getOffer(mainStayApply.getId(),3);
                    exportParam.setMainStayOffer(mainStayOffer);
                    //转案
                    TransferCase mainStayCase=new TransferCase();
                    mainStayCase.setSaleToCopyDate(getCaseDate(apply.getId(),1,3));
                    mainStayCase.setCopyToConnectDate(getCaseDate(apply.getId(),2,3));
                    mainStayCase.setConnectToCopyDate(getCaseDate(apply.getId(),3,3));
                    exportParam.setMainStayCase(mainStayCase);

                }

                //语言住宿
                //住宿
                Apply langStayApply=getStayInfo(apply.getId(),4);
                if(langStayApply.getId()!=null){
                    exportParam.setLangStayApply(langStayApply);
                    Reply langStayReply=setOfferReply(langStayApply.getId());
                    if(langStayReply.getId()!=null){
                        DateParam replyResult=setParamsReply(langStayApply.getId());
                        exportParam.setLangStayReplyDate(replyResult);
                    }
                    String visitRecord= getVisit(mainStayApply.getId(),2);
                    exportParam.setLangStayVisit(visitRecord);
                    Offer langStayOffer=getOffer(langStayApply.getId(),4);
                    exportParam.setMainStayOffer(langStayOffer);

                    //转案
                    TransferCase langStayCase=new TransferCase();
                    langStayCase.setSaleToCopyDate(getCaseDate(apply.getId(),1,4));
                    langStayCase.setCopyToConnectDate(getCaseDate(apply.getId(),2,4));
                    langStayCase.setConnectToCopyDate(getCaseDate(apply.getId(),3,4));
                    exportParam.setLangStayCase(langStayCase);
                }

                //监护
                Apply custodyApply=getStayInfo(apply.getId(),5);
                if(custodyApply.getId()!=null){
                    exportParam.setCustodyApply(custodyApply);
                    Reply langStayReply=setOfferReply(custodyApply.getId());
                    if(langStayReply.getId()!=null){
                        DateParam custodyStayResult=setParamsReply(langStayApply.getId());
                        exportParam.setCustodyReplyDate(custodyStayResult);
                    }
                    String visitRecord= getVisit(custodyApply.getId(),2);
                    exportParam.setCustodyVisit(visitRecord);
                    Offer custodyOffer=getOffer(custodyApply.getId(),5);
                    exportParam.setCustodyOffer(custodyOffer);

                    //转案
                    TransferCase langStayCase=new TransferCase();
                    langStayCase.setSaleToCopyDate(getCaseDate(apply.getId(),1,4));
                    langStayCase.setCopyToConnectDate(getCaseDate(apply.getId(),2,4));
                    langStayCase.setConnectToCopyDate(getCaseDate(apply.getId(),3,4));
                    exportParam.setLangStayCase(langStayCase);
                }

            }
            StudentInfo student=studentInfoService.getStudentInfoById(apply.getStudentId());
            if(student.getId()!=null && student.getEducation()!=null){
                Degree degree=degreeService.queryById(student.getEducation());
                if(degree!=null && degree.getDegreeId()!=null && org.springframework.util.StringUtils.hasText(degree.getDegree())){
                    student.setEducationName(degree.getDegree());
                }
            }
            Experience experience=new Experience();
            experience.setStudentId(student.getId());
            List<Experience> experiences=experienceService.getList(experience);
            if(experiences.size()>0){
                String experienceName="";
                String positionName="";
                for(int i=0;i<experiences.size();i++){
                    Experience exp=experiences.get(i);
                    if(null!=exp.getWorkCompany() && null!=exp.getWorkStartDate() && null!=exp.getWorkEndDate()) {
                        String preString = "";
                        if (!"".equals(experienceName)) {
                            preString = "；";
                        }
                        experienceName = experienceName + preString + new SimpleDateFormat("yyyy-MM-dd").format(exp.getWorkStartDate())
                                + "--" + new SimpleDateFormat("yyyy-MM-dd").format(exp.getWorkEndDate()) + " " +
                                exp.getWorkCompany();
                        if (!"".equals(positionName)) {
                            preString = "；";
                        }
                        positionName = positionName + preString + exp.getWorkPosition();
                        if (exp.getWorkStatus() != null) {
                            if (exp.getWorkStatus() == 1) {
                                student.setWorkStatus("工作经验");
                            } else {
                                student.setWorkStatus("实习经历");
                            }
                        }
                    }
                }
                student.setWorkPosition(positionName);
                student.setExperiencename(experienceName);
            }
            List<Exam> exams=examService.getList(student.getId());
            if(exams.size()>0){
                String examScore="";
                String examTime="";
                for(int i=0;i<exams.size();i++){
                    Exam exam=exams.get(i);
                    if(exam.getExamScore()!=null){
                        if(exam.getExamTime()!=null){
                            examTime=new SimpleDateFormat("yyyy年MM月").format(exam.getExamTime());
                        }
                        examScore=examScore+examTime+":"+exam.getExamScore()+" ";
                    }
                    student.setGpa(exam.getGpa());
                    student.setExamType(exam.getExamType());
                }
                student.setExamScore(examScore);
            }
            Contract contract=contractService.getContractById(apply.getContractId());
            if(contract.getId()!=null){
                Refund refund=new Refund();
                refund.setContractId(contract.getId());
               List<Refund> refunds= refundService.getList(refund);
               if(refunds.size()>0){
                   exportParam.setRefund(refunds.get(0));
               }
            }
            exportParam.setStudent(student);
            exportParam.setContract(contract);


            //后续服务
            AfterBusiness business=getAfterService(contract.getId());
            exportParam.setAfterBusiness(business);
            //文案信息
            exportParam.setCopywriting(setCopyWriting(apply.getStudentId()));
            exportParam.setVisa(getVisa(apply.getStudentId()));

            //转案时间和人员
            String saleName=getOperators(apply.getId(),1);
            String applyCopyName=getOperators(apply.getId(),2);
            String copyName=getOperators(apply.getId(),3);
            String visaName=getOperators(apply.getId(),4);
            String connectName=getOperators(apply.getId(),5);


            exportParam.setSaleOperator(saleName);
            exportParam.setApplyCopyOperator(applyCopyName);
            exportParam.setCopyOperator(copyName);
            exportParam.setConnectOperator(connectName);
            exportParam.setVisaOperator(visaName);




            List<Plan> plans=planService.getList(contract.getId());
            String planName="";
            for(int i=0;i<plans.size();i++){
                Plan plan=plans.get(i);
                if(plan.getCollegeName()!=null){
                    planName=planName+plan.getCollegeName();
                }
            }
            exportParam.setCollegePlan(planName);
            exports.add(exportParam);

        }
        return exports;
       // return new ExportUtil().export(exports,columns);
       // return new ExportUtil().export(new ParseEntity().parse(params),columns);
    }

    /**
     * 查询申请记录列表
     * @param countryId
     * @param memberId
     * @param startDate
     * @return
     */
    private List<Apply> getApply( MemberApply memberApply) {

        List<MemberApply> memberApplies=getMemberApply(memberApply);

        List<Apply> applyList=new ArrayList<>();
        for(MemberApply memberApplyInfo:memberApplies){
            Apply apply= applyService.getApplyById(memberApplyInfo.getApplyId());
            if(apply!=null){
                applyList.add(apply);
            }
        }
        return applyList;
    }

    /***
     * 查询员工负责的申请案子
     * @param memberApply
     * @return
     */
    private List<MemberApply> getMemberApply( MemberApply memberApply) {

        memberApply.setEnable(1);
        if(memberApply.getMemberId()!=null ){
            if( memberApply.getMemberId()==0){
                memberApply.setMemberId(null);
            }else{
                memberApply.setMemberId(memberApply.getMemberId());
            }
        }
        return memberApplyService.getList(memberApply);
    }

    private Bonus getBonus(Integer applyId, int businessCase) {
        Bonus bonus=new Bonus();
        bonus.setApplyId(applyId);
        bonus.setBusinessCase(businessCase);
        List<Bonus> bonuses=bonusService.getList(bonus);
        if(bonuses.size()>0){
            return bonuses.get(0);
        }
        return bonus;
    }

    private Offer getOffer(Integer applyId,Integer type) {
        Offer offer=new Offer();
        offer.setApplyId(applyId);
        offer.setOfferType(type);
        List<Offer>offers=offerService.getList(offer);
        if(offers.size()>0){
            return offers.get(0);
        }
        return offer;
    }

    private String getVisit(Integer applyId, int visitType) {
        String content="";
        Visit visit=new Visit();
        visit.setApplyId(applyId);
        visit.setVisitType(visitType);
        List<Visit> visits=visitService.getList(visit);
        if(visits.size()>0){
            for(int i=0;i<visits.size();i++){
                Visit visitData=visits.get(i);
                if(visitData.getVisitDate()!=null &&  visitData.getContent()!=null){
                    content=new SimpleDateFormat("yyyy-MM-dd").format(visitData.getVisitDate())+":"
                    +visitData.getContent()+"；";
                }
            }
        }
        return content;
    }

    private String getOperators(Integer id,Integer role) {
        String operatorName="";
        MemberApply apply=new MemberApply();
        apply.setApplyId(id);
        apply.setMemberRole(role);
        List<MemberApply> operators= memberApplyService.getList(apply);
        if(operators.size()>0){
            for(int i=0;i<operators.size();i++){
                MemberApply operatorIndex=operators.get(i);
                if(operatorIndex!=null && operatorIndex.getMemberName()!=null){
                    operatorName=operatorName+operators.get(i).getMemberName();
                }
            }
        }
        return operatorName;
    }

    private AfterBusiness getAfterService(Integer id) {
        AfterBusiness afterBusiness=new AfterBusiness();
        afterBusiness.setContractId(id);
        List<AfterBusiness> businesses=afterBusinessService.getList(afterBusiness);
        if(businesses.size()>0){
            return businesses.get(0);
        }
        return afterBusiness;
    }


    private Apply getStayInfo(Integer applyId,Integer applyType) {
        Apply apply=new Apply();
        apply.setApplyType(applyType);
        apply.setMainRelation(applyId);
        List<Apply> applyList=applyService.getList(apply);
        if(applyList.size()>0){
            return applyList.get(0);
        }else{
            return apply;
        }
    }

    private DateParam setParamsReply(Integer applyId) {
        DateParam dateParam=new DateParam();

        Reply reply=getReply(applyId,1);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setStudentConfirmApplyDate(reply.getReplyDate()); //学生确认申请
        }
         reply=getReply(applyId,2);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setSchoolConfirmReceiveApplyDate(reply.getReplyDate()); //学校确认收到申请
        }
        reply=getReply(applyId,3);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setSchoolRequireDate(reply.getReplyDate()); //学校要求补件日期
            if(reply.getProvideDeadline()!=null){
                dateParam.setSchoolRequireAddDeadline(reply.getProvideDeadline());//要求补件截止日
            }
            Reply replyConfirm=getStuReplyOffer(reply.getId());
            if(replyConfirm.getId()!=null && replyConfirm.getReplyDate()!=null){
                dateParam.setStudentConfirmSupplementDate(replyConfirm.getReplyDate()); //学生确认补件日期
            }
        }

        reply=getReply(applyId,4);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setSchoolConfirmStudentSupplementDate(reply.getReplyDate()); //学校确认补件日期
        }
        reply=getReply(applyId,5);
        if(reply.getId()!=null && reply.getReplyDate()!=null && reply.getReplyResult()!=null){
            dateParam.setConditionOfferDate(reply.getReplyDate()); //Offer到达日期
            dateParam.setReplyResult(reply.getReplyResult()==1?"录取":"拒绝"); //录取结果
            dateParam.setReplyReason(reply.getReplyReason()==null?"":reply.getReplyReason()); //拒绝原因
            if(reply.getReplyDeadline()!=null){
                dateParam.setSchoolRequireConditionOfferDeadline(reply.getReplyDeadline()); //截止日期
            }
            Reply replyConfirm=getStuReplyOffer(reply.getId());
            if(replyConfirm.getId()!=null && replyConfirm.getReplyDate()!=null){
                dateParam.setStudentConfirmOfferDate(replyConfirm.getReplyDate());//学生回复offer日期
            }
            Reply schoolReplyConfirm=getStuReplyOffer(replyConfirm.getId());
            if(schoolReplyConfirm.getId()!=null && schoolReplyConfirm.getReplyDate()!=null){
                dateParam.setSchoolConfirmOfferDate(schoolReplyConfirm.getReplyDate());//学校回复学生offer日期
            }
        }
        reply=getReply(applyId,6);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setUnConditionDate(reply.getReplyDate()); //无条件offer到达日期
            Reply replyConfirm=getStuReplyOffer(reply.getId());
            if(replyConfirm.getId()!=null && replyConfirm.getReplyDate()!=null){
                dateParam.setStudentConfirmUnConditionDate(replyConfirm.getReplyDate());//学生回复无条件offer日期
            }
            Reply schoolReplyConfirm=getStuReplyOffer(replyConfirm.getId());
            if(schoolReplyConfirm.getId()!=null && schoolReplyConfirm.getReplyDate()!=null){
                dateParam.setSchoolConfirmStuUnConditionDate(schoolReplyConfirm.getReplyDate());//学校回复学生无条件offer日期
            }
        }
        reply=getReply(applyId,7);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setCoeDate(reply.getReplyDate());  //COE电子版到达日期
        }
        reply=getReply(applyId,8);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setOriginalCoeDate(reply.getReplyDate());  //COE原件到达日期
        }

        reply=getReply(applyId,9);
        if(reply.getId()!=null && reply.getReplyDate()!=null){
            dateParam.setDelayDate(reply.getReplyDate());  //offer延期到达日期
            if(reply.getReplyDeadline()!=null){
                dateParam.setDelayReplyDeadline(reply.getReplyDeadline()); //offfer延期回复截止日
            }
            if(reply.getProvideDeadline()!=null){
                dateParam.setDelayReplyDate(reply.getProvideDeadline());  //延期开学日
            }
        }
        return dateParam;
    }

    private Reply getReply(Integer applyId, int businessCase) {
        Reply reply=new Reply();
        reply.setApplyId(applyId);
        reply.setReplyType(businessCase);
        List<Reply> replyList=replyService.getList(reply);
        if(replyList.size()>0){
            return replyList.get(0);
        }
        return reply;
    }

    //获得offer到达信息
    private Reply setOfferReply(Integer applyId) {
        Reply reply=new Reply();
        reply.setApplyId(applyId);
        reply.setReplyType(1); //offer到达
        return setStuOfferReply(reply);
    }
    private Reply setStuOfferReply(Reply reply) {
        List<Reply> replyList=replyService.getList(reply);
        if(replyList.size()>0){
            return replyList.get(0);
        }else{
            return reply;
        }
    }
    //获得无条件offer到达信息
    private Reply setStuOfferReply(Integer applyId) {
        Reply reply=new Reply();
        reply.setApplyId(applyId);
        reply.setReplyType(2); //offer到达
        return setStuOfferReply(reply);
    }
    private Reply getStuReplyOffer(Integer replyId){
        Reply reply=new Reply();
        reply.setReplyId(replyId);
        return setStuOfferReply(reply);
    }


    private Visa getVisa(Integer studentId) {
        Visa visa=new Visa();
        visa.setStudentId(studentId);
        List<Visa> visas=visaService.getList(visa);
        if(visas.size()>0){
            return visas.get(0);
        }else{
            return visa;
        }
    }

    private List<TransferCase> getCases(List<Integer>oaIds,int id) {

       return transferCaseService.queryByOperatorId(oaIds,id);
    }


    private Date getCaseDate(int applyId,int businessCase,int type) {
        TransferCase transferCase=new TransferCase();
        transferCase.setApplyId(applyId);
        //transferCase.setTransferType(type);
        transferCase.setBussinessCase(businessCase);
        List<TransferCase> cases= transferCaseService.getOperator(transferCase);
        if(cases.size()>0){
           return  cases.get(0).getSendDate();
        }
        return null;
    }
    private StudentCopywriting setCopyWriting(Integer studentId) {
        StudentCopywriting copywriting=new StudentCopywriting();
        copywriting.setStudentId(studentId);
        List<StudentCopywriting> copywritingList= copywritingService.getList(copywriting);
        if(copywritingList.size()>0){
            return copywritingList.get(0);
        }else{
            return copywriting;
        }
    }

    private List<Map<String,Object>> list2Map(List<StudentInfo> studentInfos) {
        List<Map<String,Object>> params=new ArrayList<>();
        for(StudentInfo student:studentInfos){
            Map<String,Object> param=new HashMap<>();
            param.put("name",student.getName());
            param.put("schoolNo",student.getSchoolNo());
            param.put("systemNo",student.getSystemNo());
            param.put("gender",student.getGender());
            param.put("birthday",student.getBirthday());
            param.put("branchId",student.getBranchId());
            param.put("vipStatus",student.getVipStatus());
            param.put("graduationStatus",student.getGraduationStatus());

            param.put("school",student.getSchool());
            param.put("education",student.getEducation());
            param.put("major",student.getMajor());
            param.put("grade",student.getGrade());
          /*  param.put("emailAccount",student.getEmailAccount());
            param.put("emailPassword",student.getEmailPassword());*/
            params.add(param);
        }
        return  params;
    }


    public Supplement getSupplement(Integer applyId) {
        Supplement supplement=new Supplement();
        //邮寄申请材料
        List<Supplement> supplementList=getSupplementInfo(applyId,1);
        //补件信息
        List<Supplement> addSupplementList=getSupplementInfo(applyId,2);
        //邮寄最终成绩单
        List<Supplement> scoreSupplementList=getSupplementInfo(applyId,3);
        //录取包裹
        List<Supplement> admissionSupplementList=getSupplementInfo(applyId,4);
        //i20原件
        List<Supplement> i20SupplementList=getSupplementInfo(applyId,5);
        if(supplementList.size()>0){
            Supplement material =supplementList.get(0);
            if(material.getCollectMaterialDate()!=null){
                supplement.setCollectMaterialDate(material.getCollectMaterialDate());
            }
            if(material.getExpressNumber()!=null){
                supplement.setExpressNumber(material.getExpressNumber());
            }
            if(material.getExpressStatus()!=null){
                supplement.setExpressStatus(material.getExpressStatus());
            }
        }
        if(scoreSupplementList.size()>0 && scoreSupplementList.get(0).getSupplementDate()!=null){
           supplement.setSendScoreDate(scoreSupplementList.get(0).getSupplementDate());
           if(scoreSupplementList.get(0).getExpressNumber()!=null){
                supplement.setSendScoreNo(scoreSupplementList.get(0).getExpressNumber());
            }
        }
        String content="";
        if(addSupplementList.size()>0){
            for(int i=0;i<addSupplementList.size();i++){
                Supplement add=addSupplementList.get(i);
                if(add.getSupplementDate()!=null && add.getSupplementContent()!=null){
                    content=content+add.getSupplementDate()+":"+add.getSupplementContent()+"；";
                }
            }
        }
        supplement.setSupplementContent(content);

        if(admissionSupplementList.size()>0){
            if(admissionSupplementList.get(0).getExpressNumber()!=null){
                supplement.setAdmissionExpressNo(admissionSupplementList.get(0).getExpressNumber());
            }
            if(addSupplementList.get(0).getSupplementDate()!=null){
                supplement.setAdmissionDate(admissionSupplementList.get(0).getSupplementDate());
            }
        }
        if(i20SupplementList.size()>0){
            if(i20SupplementList.get(0).getExpressNumber()!=null){
                supplement.setI20ExpressNo(i20SupplementList.get(0).getExpressNumber());
            }
        }
        return supplement;
    }

    private List<Supplement> getSupplementInfo(Integer applyId,Integer businessCase){
        Supplement supplement=new Supplement();
        supplement.setApplyId(applyId);
        supplement.setBussinessCase(1);
        return supplementService.getList(supplement);
    }


}
