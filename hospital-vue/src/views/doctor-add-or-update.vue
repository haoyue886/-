<template>
	<!-- 
	:title="!dataForm.id ? '新增' : '修改'"	标题文字 	dataForm.id等于0表示新增,不等于表示修改
	v-if="isAuth(['ROOT', 'DOCTOR:INSERT', 'DOCTOR:UPDATE'])"	访问需要的权限
	:close-on-click-modal="false"	点击外围阴影是否关闭
	v-model="visible"	引用visible变量	true就是打开,false关闭
	 -->
    <el-dialog
        :title="!dataForm.id ? '新增' : '修改'"
        v-if="isAuth(['ROOT', 'DOCTOR:INSERT', 'DOCTOR:UPDATE'])"
        :close-on-click-modal="false"
        v-model="visible"
        width="480px"
    >
		<!-- 
		dataForm:	前端引用这个变量
		:rules="dataRule"	绑定表单验证
		 -->
        <el-scrollbar height="500px">
            <el-form :model="dataForm" ref="dataForm" :rules="dataRule" label-width="80px">
                <el-form-item label="姓名" prop="name"><el-input v-model="dataForm.name" clearable /></el-form-item>
                <el-form-item label="身份证号" prop="pid"><el-input v-model="dataForm.pid" clearable /></el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="dataForm.sex">
                        <el-radio-button label="男" />
                        <el-radio-button label="女" />
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="出生日期" prop="birthday">
                    <el-date-picker
                        v-model="dataForm.birthday"
                        type="date"
                        placeholder="选择日期"
                        :editable="false"
                        format="YYYY-MM-DD"
                        value-format="YYYY-MM-DD"
                        style="width: 100%;"
                    />
                </el-form-item>
                <el-form-item label="毕业学校" prop="school">
                    <el-input v-model="dataForm.school" maxlength="50" clearable />
                </el-form-item>
                <el-form-item label="学历">
                    <el-radio-group v-model="dataForm.degree">
                        <el-radio-button label="博士" />
                        <el-radio-button label="研究生" />
                        <el-radio-button label="本科" />
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="电话" prop="tel"><el-input v-model="dataForm.tel" clearable /></el-form-item>
                <el-form-item label="家庭住址" prop="address">
                    <el-input v-model="dataForm.address" maxlength="200" clearable />
                </el-form-item>
                <el-form-item label="电子信箱" prop="email">
                    <el-input v-model="dataForm.email" clearable />
                </el-form-item>
                <el-form-item label="职务" prop="job">
                    <el-select v-model="dataForm.job" clearable>
                        <el-option label="主任医师" value="主任医师"></el-option>
                        <el-option label="副主任医师" value="副主任医师"></el-option>
                        <el-option label="主治医师" value="主治医师"></el-option>
                        <el-option label="副主治医师" value="副主治医师"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="科室部门" prop="deptSub">
                    <el-cascader v-model="dataForm.deptSub" :options="dept" clearable />
                </el-form-item>
                <el-form-item label="备注信息" prop="remark">
                    <el-input v-model="dataForm.remark" maxlength="50" clearable />
                </el-form-item>
                <el-form-item label="医师介绍" prop="description">
                    <el-input
                        v-model="dataForm.description"
                        type="textarea"
                        :rows="6"
                        style="width:100%"
                        maxlength="350"
                        show-word-limit
                        clearable
                    />
                </el-form-item>
                <el-form-item label="入职日期" prop="hiredate">
                    <el-date-picker
                        v-model="dataForm.hiredate"
                        type="date"
                        placeholder="选择日期"
                        :editable="false"
                        format="YYYY-MM-DD"
                        value-format="YYYY-MM-DD"
                        style="width: 100%;"
                    />
                </el-form-item>
                <el-form-item label="标签">
					<!-- 
					 v-model="newTag"	绑定到newTag属性
					 @keyup.enter="inputTagHandle"	回车调用inputTagHandle函数
					 -->
                    <el-input v-model="newTag" @keyup.enter="inputTagHandle" clearable />
                    <p>
						<!-- 
						 :disable-transitions="false"	el-tag显示或关闭是否带动画,false表示带动画
						 @close="closeTagHandle(one)"	点击x的回调函数
						 -->
                        <el-tag
                            v-for="one in dataForm.tag"
                            :key="one"
                            closable
                            :disable-transitions="false"
                            @close="closeTagHandle(one)"
                        >
                            {{ one }}
                        </el-tag>
                    </p>
                </el-form-item>
                <el-form-item label="推荐等级">
                    <el-radio-group v-model="dataForm.recommended">
                        <el-radio-button label="推荐" />
                        <el-radio-button label="普通" />
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="状态">
                    <el-radio-group v-model="dataForm.status">
                        <el-radio-button label="在职" />
                        <el-radio-button label="离职" />
                        <el-radio-button label="退休" />
                    </el-radio-group>
                </el-form-item>
            </el-form>
        </el-scrollbar>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import dayjs from 'dayjs';
export default {
    data: function() {
        return {
			// 对话框是否显示
            visible: false,
			// 保存医生标签
            newTag: null,
			// 诊室,科室二级列表
            dept: [],
			// 绑定表单
            dataForm: {
                id: null,
                name: null,
                pid: null,
                sex: '男',
                photo: null,
                birthday: null,
                school: null,
                degree: '博士',
                tel: null,
                address: null,
                email: null,
                job: null,
                deptSub: null,
                deptSubId: null,
                remark: null,
                description: null,
                hiredate: null,
                tag: [],
                recommended: '普通',
                status: '在职'
            },
			// 表单验证规则
            dataRule: {
                name: [
                    { required: true, message: '姓名不能为空' },
                    {
                        pattern: '^[\\u4e00-\\u9fa5]{2,20}$',
                        message: '姓名格式错误'
                    }
                ],
                pid: [
                    {
                        required: true,
                        message: '身份证号不能为空'
                    },
                    {
                        pattern:
                            '^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$',
                        message: '身份证号不正确'
                    }
                ],
                birthday: [
                    {
                        required: true,
                        message: '出生日期不能为空'
                    }
                ],
                school: [
                    {
                        required: true,
                        message: '毕业学校不能为空'
                    }
                ],
                tel: [
                    { required: true, message: '电话不能为空' },
                    {
                        pattern: '^1[1-9][0-9]{9}$',
                        message: '电话格式错误'
                    }
                ],
                address: [
                    {
                        required: true,
                        message: '家庭住址不能为空'
                    }
                ],
                email: [
                    {
                        required: true,
                        message: '电子信箱不能为空'
                    },
                    {
                        pattern: '^([a-zA-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$',
                        message: '电子信箱格式错误'
                    }
                ],
                job: [
                    {
                        required: true,
                        message: '职务不能为空'
                    }
                ],
                deptSub: [
                    {
                        required: true,
                        message: '科室部门不能为空'
                    }
                ],
                remark: [
                    {
                        required: true,
                        message: '备注信息不能为空'
                    }
                ],
                description: [
                    {
                        required: true,
                        message: '医师介绍不能为空'
                    }
                ],
                hiredate: [{ required: true, message: '入职日期不能为空' }]
            }
        };
    },
    methods: {
		// 加载二级列表
        loadDeptAndSub: function() {
			let that = this;
			// 发起异步请求
			that.$http('/medical/dept/searchDeptAndSub', 'GET', {}, false, function(resp) {
				// 得到返回的科室列表
				let result = resp.result;
				// 定义dept用于保存转换后的值
				let dept = [];
				// 遍历所有的科室		one:科室名称
				for (let one in result) {
					let array = [];
					// 通过科室名称获取他下面所有的诊室,并遍历
					for (let sub of result[one]) {
						// 添加到array
						array.push({
							value: sub.subId,
							label: sub.subName
						});
					}
					// 封装成ElementPlus二级列表需要的格式
					dept.push({
						value: one,
						label: one,
						children: array
					});
				}
				// 赋值
				that.dept = dept;
			});
		},
		// 重置表单
		reset: function() {
		    let dataForm = {
		        id: null,
		        name: null,
		        pid: null,
		        sex: '男',
		        photo: null,
		        birthday: null,
		        school: null,
		        degree: '博士',
		        tel: null,
		        address: null,
		        email: null,
		        job: null,
		        deptSub: null,
		        deptSubId: null,
		        remark: null,
		        description: null,
		        hiredate: null,
		        tag: [],
		        recommended: '普通',
		        status: '在职'
		    };
		    this.dataForm = dataForm;
			// 清空标签输入框
			this.newTag = null;
		},
		// 初始化函数,如果有传id则是修改,没有传则是0,为新增
		init: function(id) {
		    let that = this;
		    //重置表单控件
		    that.reset();
		    //如果id是undefined，就对模型层id变量赋值为0
		    that.dataForm.id = id || 0;
			
		    //显示对话框
		    that.visible = true;
		  
		    //DOM渲染操作要放在$nextTick函数中执行，例如加载数据
		    that.$nextTick(() => {
		        //清理前端验证结果
		        that.$refs['dataForm'].resetFields();
		        //加载二级列表数据
		        that.loadDeptAndSub();
			
				//下方是新添加的内容
				// 判断是否有id	在js中0可以表示false,不为0的数字可以表示true
				if (that.dataForm.id) {
					// 发起异步请求,通过医生id查询详情
					that.$http('/doctor/searchById', 'POST', { id: id }, true, function(resp) {
						// 赋值
						
						let result = resp;
						let json = {
							'1': '在职',
							'2': '离职',
							'3': '退休'
						};
						// that.dataForm.name = "中国和";
						that.dataForm.name = result.name;
						that.dataForm.pid = result.pid;
						that.dataForm.sex = result.sex;
						that.dataForm.birthday = result.birthday;
						that.dataForm.school = result.school;
						that.dataForm.degree = result.degree;
						that.dataForm.tel = result.tel;
						that.dataForm.address = result.address;
						that.dataForm.email = result.email;
						that.dataForm.job = result.job;
						that.dataForm.remark = result.remark;
						that.dataForm.description = result.description;
						that.dataForm.hiredate = result.hiredate;
						// 为1就是推荐	2普通
						that.dataForm.recommended = result.recommended ? '推荐' : '普通';
						that.dataForm.tag = result.tag;
						// 将后台返回的数字传为汉字
						that.dataForm.status = json[result.status + ''];
						// that.dataForm.deptSub==>[科室id,诊室id]
						that.dataForm.deptSub = [result.deptName, result.deptSubId];
					});
				}
		    });
		},
		// 标签输入框回车事件
		inputTagHandle: function() {
			// 判断不为空
			if (this.newTag != null && this.newTag != '') {
				// 判断是否已经存在这个标签
				if (this.dataForm.tag.includes(this.newTag)) {
					ElMessage({
						message: '不能添加重复标签',
						type: 'warning',
						duration: 1200
					});
				} else {
					// 添加到this.dataForm.tag	清空this.newTag
					this.dataForm.tag.push(this.newTag);
					this.newTag = null;
				}
			}
		},
		// tag点击x事件
		closeTagHandle: function(tag) {
			let index = this.dataForm.tag.indexOf(tag);
			this.dataForm.tag.splice(index, 1);
		},
		// 表单确认按钮
		dataFormSubmit: function() {
			let that = this;
			// 前端表单验证
			that.$refs['dataForm'].validate(function(valid) {
				// 判断是否通过验证
				if (valid) {
					// deptSub==>[科室id,诊室id]	取第二个	诊室id
					that.dataForm.deptSubId = that.dataForm.deptSub[1];
					let json = {
						在职: 1,
						离职: 2,
						退休: 3
					};
                    console.log("进入data赋值");
					// 提交到后台的数据
					let data = {
						id: that.dataForm.id,
						name: that.dataForm.name,
						pid: that.dataForm.pid,
						sex: that.dataForm.sex,
						birthday: that.dataForm.birthday,
						school: that.dataForm.school,
						degree: that.dataForm.degree,
						tel: that.dataForm.tel,
						address: that.dataForm.address,
						email: that.dataForm.email,
						job: that.dataForm.job,
						remark: that.dataForm.remark,
						description: that.dataForm.description,
						// 日期对象中提取年月日字符串传到后端
						hiredate: dayjs(that.dataForm.hiredate).format('YYYY-MM-DD'),
						tag: that.dataForm.tag,
						// 三元运算符	为推荐:1		反之:2
						recommended: that.dataForm.recommended == '推荐' ? 1 : 0,
						// 将汉字转数字
						status: json[that.dataForm.status],
						// 诊室id在上面已经转换好了
						subId: that.dataForm.deptSubId
					};
                   
					// 打开遮罩层
					// that.$openLoading();
					/**
					 * 发起异步请求
					 * ${!that.dataForm.id ? 'insert' : 'update'}	判断是否等于0,等于就是新增
					 * 因为在js中0可以表示false,不为0的数字可以表示true
					 * 因为在打开时为0新增
					 */
                     
					that.$http(`/doctor/${!that.dataForm.id ? 'insert' : 'update'}`, 'POST', data, true, function (resp) {
                        
						// 关闭遮罩层
						// that.$closeLoading();
						
						ElMessage({
							message: '操作成功',
							type: 'success'
						});
						// 关闭对话框
						that.visible = false;
						/**
						 * 需要刷新doctor页面
						 * that.$emit('refreshDataList');	通过这个触发refreshDataList事件,在doctor页面捕获
						 * @refreshDataList="loadDataList" 通过这个捕获,然后执行loadDataList()函数
						 */	
						that.$emit('refreshDataList');
					});
                   
				}
			})
		},
	}
	
};
</script>

<style lang="less" scoped="scoped"></style>
