package com.erp.model;

import javax.persistence.*;
import java.util.Date;


@Table(name = "apply_supplement_record_relation")
public class Supplement
{
	/**id*/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID")
	private Integer id;
	/**申请id*/
    @Column(name ="APPLY_ID")
	private Integer applyId;
	/**北京寄出日期*/
    @Column(name ="BEIJING_SEND_DATE")
	private Date beijingSendDate;
	/**总部寄出日期*/
    @Column(name ="HEADQUARTERS_SEND_DATE")
	private Date headquartersSendDate;
	/**快递公司*/
    @Column(name ="EXPRESS_COMPANY")
	private String expressCompany;
	/**快递号*/
    @Column(name ="EXPRESS_NUMBER")
	private String expressNumber;
	/**expressContent*/
    @Column(name ="EXPRESS_CONTENT")
	private String expressContent;
	/**递交材料方式：1-传真 2-扫描 3-邮寄 4-现场递交*/
    @Column(name ="SEND_MATERIAL_TYPE")
	private String sendMaterialType;
	/**业务场景:1-首次寄出材料 2-寄出补件内容 3-寄出成绩单/疫苗表 4-校方寄出coe原件*/
    @Column(name ="bussiness_case")
	private Integer bussinessCase;

    /**收材料日期**/
    @Column(name="collect_material_date")
	private Date collectMaterialDate;

    /**补件日期**/
    @Column(name="supplement_date")
	private Date supplementDate;

    @Column(name="express_status")
	private  String expressStatus;
/**寄件内容，供前台显示**/
    @Transient
	private String supplementContent;
/**寄出最终成绩单日期**/
	@Transient
	private Date sendScoreDate;
	/**寄出最终成绩单快递单号**/
	@Transient
	private String sendScoreNo;
	/**i20原件单号**/
	@Transient
	private String i20ExpressNo;
	/**录取包裹单号**/
	@Transient
	private String admissionExpressNo;
	/**录取包裹到达日期**/
	@Transient
	private Date admissionDate;
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */

	public Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(Integer id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申请id
	 */

	public Integer getApplyId(){
		return this.applyId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请id
	 */
	public void setApplyId(Integer applyId){
		this.applyId = applyId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  北京寄出日期
	 */

	public Date getBeijingSendDate(){
		return this.beijingSendDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  北京寄出日期
	 */
	public void setBeijingSendDate(Date beijingSendDate){
		this.beijingSendDate = beijingSendDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  总部寄出日期
	 */

	public Date getHeadquartersSendDate(){
		return this.headquartersSendDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  总部寄出日期
	 */
	public void setHeadquartersSendDate(Date headquartersSendDate){
		this.headquartersSendDate = headquartersSendDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  快递公司
	 */

	public String getExpressCompany(){
		return this.expressCompany;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  快递公司
	 */
	public void setExpressCompany(String expressCompany){
		this.expressCompany = expressCompany;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  快递号
	 */

	public String getExpressNumber(){
		return this.expressNumber;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  快递号
	 */
	public void setExpressNumber(String expressNumber){
		this.expressNumber = expressNumber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  expressContent
	 */

	public String getExpressContent(){
		return this.expressContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  expressContent
	 */
	public void setExpressContent(String expressContent){
		this.expressContent = expressContent;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  递交材料方式：1-传真 2-扫描 3-邮寄 4-现场递交
	 */

	public String getSendMaterialType(){
		return this.sendMaterialType;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  递交材料方式：1-传真 2-扫描 3-邮寄 4-现场递交
	 */
	public void setSendMaterialType(String sendMaterialType){
		this.sendMaterialType = sendMaterialType;
	}

	public Integer getBussinessCase() {
		return bussinessCase;
	}

	public void setBussinessCase(Integer bussinessCase) {
		this.bussinessCase = bussinessCase;
	}


	public Date getCollectMaterialDate() {
		return collectMaterialDate;
	}

	public void setCollectMaterialDate(Date collectMaterialDate) {
		this.collectMaterialDate = collectMaterialDate;
	}

	public Date getSupplementDate() {
		return supplementDate;
	}

	public void setSupplementDate(Date supplementDate) {
		this.supplementDate = supplementDate;
	}

	public String getSupplementContent() {
		return supplementContent;
	}

	public void setSupplementContent(String supplementContent) {
		this.supplementContent = supplementContent;
	}

	public Date getSendScoreDate() {
		return sendScoreDate;
	}

	public void setSendScoreDate(Date sendScoreDate) {
		this.sendScoreDate = sendScoreDate;
	}

	public String getSendScoreNo() {
		return sendScoreNo;
	}

	public void setSendScoreNo(String sendScoreNo) {
		this.sendScoreNo = sendScoreNo;
	}

	public String getI20ExpressNo() {
		return i20ExpressNo;
	}

	public void setI20ExpressNo(String i20ExpressNo) {
		this.i20ExpressNo = i20ExpressNo;
	}

	public String getAdmissionExpressNo() {
		return admissionExpressNo;
	}

	public void setAdmissionExpressNo(String admissionExpressNo) {
		this.admissionExpressNo = admissionExpressNo;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getExpressStatus() {
		return expressStatus;
	}

	public void setExpressStatus(String expressStatus) {
		this.expressStatus = expressStatus;
	}
}
