<template>
  <q-page>
    <!-- 添加图谱数据库 -->
    <q-dialog
      v-model="addKGDBDialog"
      persistent
    >
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">图谱数据库名称</div>
        </q-card-section>

        <q-card-section class="q-ptnone">
          <!-- 课程数据库的名字 -->
          <q-input
            dense
            v-model="addKGDBName"
            autofocus
            :rules="[val => !!val || '空字段！']"
          ></q-input>
        </q-card-section>

        <q-card-actions
          align="right"
          class="text-primary"
        >
          <q-btn
            flat
            label="取消"
            v-close-popup
          ></q-btn>
            <q-btn
              flat
              label="添加"
              type="submit"
              @click="addKGDB"
            ></q-btn>
              </q-card-actions>
      </q-card>
      </q-dialog>
      <q-card
        flat
        bordered
        class="q-pa-sm q-ma-sm"
      >
        <!-- 知识点搜索框 -->
        <q-card-section class="q-pa-xs row">
          <!-- 选择是课程数据库的名称 -->
          <q-select
            use-input
            class="col-3 q-mt-none"
            style="margin-top: -0rem"
            v-model="selectKGDBName"
            @input='checkExistDB(selectKGDBName)'
            :options="DBList"
            label="图谱名称"
          >
            </q-select>
            <!-- 搜索知识点 -->
            <div class="GPLAY__toolbar-input-container row q-ma-md no-wrap col-5">
              <q-input
                dense
                outlined
                square
                v-model="searchTerm"
                placeholder="请输入知识点"
                @keydown="matchSearchByStr"
                class="bg-white col q-ma-none"
              />
              <q-btn
                @click="handleSearchClike"
                color="primary"
                class="q-ma-none"
                icon="search"
                style="height: 2.5rem"
              />
            </div>
            <!-- <q-btn
              color="secondary"
              class="absolute-top-right"
              icon="add"
              size='1.3rem'
              @click="addKGDBDialog = true"
            > -->
              <!-- <q-tooltip content-class="bg-accent">添加数据库</q-tooltip>
              </q-btn> -->
        </q-card-section>
        <q-separator inset> </q-separator>
        <q-card-section class="q-pa-none q-ma-none">
          <div class="row q-gutter-md">
            <!-- 图谱渲染 -->
            <div
              id="mynetwork"
              class="col-12 col-md-8"
              style="height: 85vh;1px solid lightgray;"
            ></div>
          <q-separator
            vertical
            size="0.2rem"
          > </q-separator>
            <!-- W语言编辑区 -->.
            <div class="full-width col">
              <q-input
                class="q-ma-xs"
                v-model="wnorl"
                filled
                autogrow
                type="textarea"
              />
              <div class="section-right-son row">
                <q-btn
                  icon="add"
                  color="green"
                  @click="upload"
                  class="q-ma-sm"
                >添加</q-btn>
                  <q-btn
                    icon="upload"
                    color="green"
                    @click="controlTimeUpdate"
                    class="q-ma-sm"
                  >更新并格式化</q-btn>
              </div>
            </div>
            </div>
        </q-card-section>
        </q-card>
  </q-page>
</template>
<script>
const vis = require("vis-network/dist/vis-network.min.js");
require("vis-network/dist/dist/vis-network.min.css");
import {
  fuzzFindConterm,
  editKG,
  fuzzFindRelterm,
  submitKgCode,
  showDB,
  createKGDB,
  matchSearchss,
  matchSearch,
  findAllDBNameAndCName,
  checkExistDBInter,
  addADBInfoInter,
} from "src/api/common/graph";
import { Loading } from "quasar";
export default {
  data() {
    let _this = this;
    return {
      //上传数据库的名称（课程名的别名）（不是课程名）
      uploadDBName: "",
      // 模糊查询的字段
      headword: "",
      // 课程图谱数据库的名称
      addKGDBName: "",
      // 存储课程名称
      DBList: [],
      //控制添加课程数据库的对话框
      addKGDBDialog: false,
      // 选择绑定的课程图谱数据库的名称
      selectKGDBName: "",
      nodeDigMode: "add",
      addNodeDig: false,
      currKonwledgeNodeDetails: {
        id: "",
        name: "",
      },
      searchTerm: "", //当前搜索的知识点
      el: "app",
      nodes: [],
      edges: [],
      // 字符串的w语言
      wnorl: "",
      // 暂存操作之前的数据w语言的json
      beforeArr: [],
      // 修改用的
      wArr: [],
      _network: {},
      container: {},
      options: {
        locale: "cn",
        manipulation: {
          initiallyActive: true,
          editEdge: {
            editWithoutDrag(data, callback) {
              _this.addAndEditEdge(data, callback);
            },
          },
          editNode(data, callback) {
            _this.nodeDigMode = "modify";
            _this.addAndEditGraphNode(data, callback);
          },
          addNode(data, callback) {
            _this.nodeDigMode = "add";
            _this.addAndEditGraphNode(data, callback);
          },
          addEdge(data, callback) {
            _this.addAndEditEdge(data, callback);
          },
          deleteNode(data, callback) {
            _this.deleteGraphNode(data, callback);
          },
        },
        edges: {
          width: 2,
          arrows: {
            to: {
              enabled: true,
              scaleFactor: 0.5,
            },
          },
        },
        nodes: {
          color: {
            border: "black",
          },
          heightConstraint: {
            valign: "middle",
          },

          font: {
            color: "white",
            align: "center",
            face: "STKaiti",
          },
          shape: "box",
        },
      },
    };
  },
  methods: {
    // 随机颜色
    randomColor() {
      var colors = [
        "#1976d2",
        "#26a69a",
        "#9c27b0",
        "#21ba45",
        "#c10015",
        "#00868B",
        "#8B6914",
        "#8A2BE2",
      ];
      let randomColor = colors[Math.floor(Math.random() * colors.length)];
      return randomColor;
    },
    //构造图谱
    createGraphInit(res) {
      // 初始化图谱
      this.nodes = res.graph.nodes;
      this.edges = res.graph.edges;
      for (let i = 0; i < this.nodes.length; i++) {
        this.nodes[i].color = this.randomColor();
      }

      var data = {
        nodes: new vis.DataSet(this.nodes),
        edges: new vis.DataSet(this.edges),
      };
      var network = new vis.Network(this.container, data, this.options);
      this._network = network;
      const _this = this;
      // 给图谱绑定单击事件
      // network.on("click", function (params) {
      //   var clickNodeId = this.getNodeAt(params.pointer.DOM); // 获取当前点击节点的id
      // });
      // 给图谱绑定双击事件
      network.on("doubleClick", async function(params) {
        var clickNodeId = this.getNodeAt(params.pointer.DOM); // 获取当前点击节点的id
        // res = {};
        for (let index = 0; index < _this.nodes.length; index++) {
          //从当前节点nodes数组中查找到点击节点获取其label
          const element = _this.nodes[index];
          if (element.id === clickNodeId) {
            let res = await _this.findConTerm(element.label); //通过label查询点击节点信息
            for (let i = 0; i < res.graph.nodes.length; i++) {
              //将请求到的数据用来扩展当前的节点数组，注意去重
              const node = res.graph.nodes[i];
              var tempflag = false;
              for (var idx2 in _this.nodes) {
                //节点去重
                if (node.id === _this.nodes[idx2].id) {
                  tempflag = true;
                  break;
                }
              }
              if (tempflag === false) {
                //节点不存在加入图谱节点数组
                node.color = _this.randomColor();
                _this.nodes.push(node);
                data.nodes.add(node);
              }
            }
            for (let i = 0; i < res.graph.edges.length; i++) {
              //将请求到的数据用来扩展当前的边数组，注意去重
              const edges = res.graph.edges[i];
              var tempflag = false;
              for (var idx2 in _this.edges) {
                //边去重
                if (edges.id === _this.edges[idx2].id) {
                  tempflag = true;
                  break;
                }
              }
              if (tempflag === false) {
                //边不存在加入图谱边数组
                _this.edges.push(edges);
                data.edges.update(res.graph.edges[i]);
              }
            }
            // res = {};
            break;
          }
        }
        // 更新W语言区域

        _this.wnorl = "";
        _this.graphTransToW(_this.nodes, _this.edges);
      });
      // 初始化更新W语言区域
      this.wnorl = "";
      this.graphTransToW(this.nodes, this.edges);
    },
    //处理搜索框点击事件
    async handleSearchClike() {
      if (this.searchTerm.trim() == "") {
        this.$q.notify({
          message: `查询知识点不能为空！`,
          timeout: 300,
          color: "negative",
          icon: "error",
          position: "bottom",
        });
        return;
      }
      let res = await this.findConTerm(this.searchTerm);
      this.createGraphInit(res);
    },
    //知识点后台查询
    async findConTerm(searchData) {
      if (this.selectKGDBName == "") {
        this.$q.notify({
          message: `请选择数据库！`,
          timeout: 300,
          color: "negative",
          icon: "error",
          position: "bottom",
        });
      }
      const data = {
        userno: "2017416616",
        conterm: searchData,
        dbname: this.uploadDBName,
      };

      let response = await editKG(data);
      console.log(response);
      if (response.data.data.termid.length === 0) {
        this.$q.notify({
          message: `查询无果！`,
          timeout: 300,
          color: "negative",
          icon: "error",
          position: "bottom",
        });
      }
      return response.data.data;
    },
    // 边的编辑
    addAndEditEdge(data, callback) {
      this.$q
        .dialog({
          title: "请输入边的名称",
          prompt: {
            model: data.label === "new" ? "" : data.label,
            type: "text",
          },
          cancel: true,
          persistent: true,
        })
        .onOk(edgeName => {
          this.saveEdgeData(edgeName, data, callback);
        })
        .onCancel(() => {
          callback(null);
        })
        .onDismiss(() => {});
    },
    // 本地保存边数据
    saveEdgeData(edgeName, data, callback) {
      if (typeof data.to === "object") data.to = data.to.id;
      if (typeof data.from === "object") data.from = data.from.id;
      data.label = edgeName;
      var str = this.edges.some(item => item.id == data.id);
      if (str) {
        //当前为修改边
        for (let i = 0; i < this.edges.length; i++) {
          if (this.edges[i].id === data.id) {
            this.edges[i].label = data.label;
          }
        }
      } else {
        //当前为添加边
        this.edges.push({
          from: data.from,
          to: data.to,
          label: data.label,
          // 传入id
          // id: data.id,
        });
      }
      this.graphTransToW(this.nodes, this.edges);
      callback(data);
    },
    // 节点的添加和编辑
    addAndEditGraphNode(data, callback) {
      this.$q
        .dialog({
          title: "请输入节点名称",
          prompt: {
            model: data.label === "new" ? "" : data.label,
            type: "text", // optional
          },
          cancel: true,
          persistent: true,
        })
        .onOk(nodeName => {
          this.saveNodeData(nodeName, data, callback);
        })
        .onCancel(() => {
          callback(null);
        })
        .onDismiss(() => {});
    },
    // 节点的删除
    deleteGraphNode(data, callback) {
      callback(data);
      this.nodes = this._network.body.data.nodes.get();
      this.edges = this._network.body.data.edges.get();
      this.graphTransToW(this.nodes, this.edges);
      var node = this.filtIndividualNodes(this.node, this.edges);
    },
    // 本地保存节点数据
    saveNodeData(nodeName, data, callback) {
      data.label = nodeName;
      var id = 0; //ID
      if (this.nodeDigMode === "modify") {
        id = data.id;
      } else {
        id = new Date().getTime().toString();
        data.id = id;
      }

      // 修改
      var isExit = this.nodes.some(item => item.id == data.id);
      if (isExit) {
        //当前为修改状态
        for (let i = 0; i < this.nodes.length; i++) {
          if (this.nodes[i].id === data.id) {
            this.nodes[i].label = data.label;
            this.graphTransToW(this.nodes, this.edges);
          }
        }
      } else {
        this.nodes.push({ id: id, label: nodeName });
        this.graphTransToW(this.nodes, this.edges);
        this.wnorl = this.wnorl + data.label;
      }

      callback(data);
    },
    // 图谱转W语言（数据转换）
    graphTransToW(Nodes, Edges) {
      // 数据转化为w语言的数组
      var w = [];
      for (let i = 0; i < Edges.length; i++) {
        var source;
        var target;
        var rel = Edges[i].label;
        // 将edges与nodes转换为W语言
        for (let j = 0; j < Nodes.length; j++) {
          if (Nodes[j].id === Edges[i].from) {
            source = Nodes[j].label;
          }
          if (Nodes[j].id === Edges[i].to) {
            target = Nodes[j].label;
          }
        }
        w.push(source + "." + rel + "+=" + target);
      }
      //////////////////////////////////////////////////////////////
      // 将已有的单个的w语言组合为单个w语言（a.b=c;a.b=d  =>  a.b=c d）
      let wCopy = w;
      for (let i = 0; i < w.length; i++) {
        w[i] = w[i].trim();
        var beforeStr = w[i].split("+=")[0];
        for (let j = i + 1; j < wCopy.length; j++) {
          wCopy[j] = wCopy[j].trim();
          if (w[j].indexOf(beforeStr) != -1) {
            w[i] = w[i] + " " + wCopy[j].split("+=")[1];
            wCopy.splice(j, 1);
            j--;
          }
        }
      }
      /////////////////////////////////////////////////////////////
      this.wArr = w;
      // console.log("++++++++++++++++++++");
      // console.log("当前的列表：" + w);
      // 将所有w语言转化为显示区域的字符串
      // console.log(w);
      this.wnorl = "";
      for (let i = 0; i < this.wArr.length; i++) {
        if (i === this.wArr.length) {
          this.wnorl = this.wnorl + this.wArr[i];
        } else {
          this.wnorl = this.wnorl + this.wArr[i] + "\n";
        }
      }
    },
    // W语言转图谱（数据转换）
    wtransToGraph(warr) {
      var temp = [];
      var nodes = [];
      var edges = [];
      var id = 1;
      // 将带有空格的组合w语言转换为多个w语言（即a.b=c d => a.b=c;a.b=d）
      for (let i = 0; i < warr.length; i++) {
        warr[i] = warr[i].trim();
        if (warr[i].indexOf(" ") != -1) {
          var splitArr = warr[i].split(" ");
          warr.push(splitArr[0]);
          for (let i = 1; i < splitArr.length; i++) {
            var wLaStr = splitArr[0].split("+=")[0] + "+=" + splitArr[i];
            warr.push(wLaStr);
          }
          warr.splice(i, 1);
          i--;
        }
      }
      // 去除W语言区域经过splite之后的多余空格字串
      // let warrtemp = [];
      // warr.forEach((item) => {
      //   if (item) warrtemp.push(item);
      // });
      // warr = warrtemp;
      // 将w语言数组转化为nodes与edges两个数组
      for (let i = 0; i < warr.length; i++) {
        var str = warr[i];
        var snode = str.split(".")[0];
        var str1 = str.split(".")[1];
        var tnode = str1.split("+=")[1];
        var rel = str1.split("+=")[0];
        if (!temp.includes(snode)) {
          nodes.push({ id: this.findNodeId(snode), label: snode });
          temp.push(snode);
        }
        if (!temp.includes(tnode)) {
          nodes.push({ id: this.findNodeId(tnode), label: tnode });
          temp.push(tnode);
        }
        // 查找连线的id
        let edgeId = this.findEdgeId(
          rel,
          this.findNodeId(snode),
          this.findNodeId(tnode)
        );
        let obj1 = nodes.find(o => o.label == snode);
        let obj2 = nodes.find(o => o.label == tnode);
        let check = edges.find(o => o.id == edgeId);
        console.log(JSON.stringify(check));
        if (JSON.stringify(check) == null) {
          edges.push({ from: obj1.id, to: obj2.id, label: rel, id: edgeId });
        }
      }
      this.nodes = nodes;
      this.edges = edges;
      return { nodes: nodes, edges: edges };
    },
    // 控制图谱与W语言实时更新
    controlTimeUpdate() {
      const _this = this;
      var text = this.wnorl;
      var warr = text.split("\n");
      // 去掉空格
      for (let i = 0; i < warr.length; i++) {
        if (warr[i] === "") {
          warr.splice(i, 1);
          i--;
        }
      }
      var data = this.wtransToGraph(warr);
      this.createGraphInit({
        graph: {
          nodes: data.nodes,
          edges: data.edges,
        },
      });
    },
    // 查找节点的id
    findNodeId(label) {
      let id = label;
      this.nodes.forEach(element => {
        if (element.label === label) {
          id = element.id;
        }
      });
      return id;
    },
    // 查找连线的id
    findEdgeId(label, sNodeId, tNodeId) {
      let id = sNodeId + "-" + tNodeId + "-" + label;
      console.log(id);
      // 查找到了就赋值
      this.edges.forEach(element => {
        if (
          element.from === sNodeId &&
          element.to === tNodeId &&
          element.label === label
        ) {
          id = element.id;
        }
      });
      // 没有就给一个时间戳
      return id;
    },
    // 整个图谱数据往服务器上传
    async upload() {
      var text = this.wnorl;
      var kgBe = this.beforeArr;
      var warr = text.split("\n");
      // 去除空格
      for (let i = 0; i < warr.length; i++) {
        if (warr[i] == "") {
          warr.splice(i, 1);
        }
      }
      // 与原来的相比
      let diff = warr.filter(function(val) {
        return kgBe.indexOf(val) === -1;
      });
      if (diff) {
        var kgCdList = diff;
        var con = [];
        var rel = [];
        for (let i = 0; i < diff.length; i++) {
          if (kgCdList[i].indexOf("+=") !== -1) {
            var tempkgcd = kgCdList[i].split("+=");
            var tempcr = tempkgcd[0].split(".");
            if (con.indexOf(tempcr[0].trim()) === -1) {
              con.push(tempcr[0].trim());
            }
            if (rel.indexOf(tempcr[1].trim()) === -1) {
              if (tempcr[1].trim() !== "child" && tempcr[1].trim() !== "ins") {
                rel.push(tempcr[1].trim());
              }
            }
            var temptri = tempkgcd[1].trim().split(" ");
            for (let j = 0; j < temptri.length; j++) {
              if (con.indexOf(temptri[j].trim()) === -1) {
                con.push(temptri[j].trim());
              }
            }
          } else if (kgcdlst[i].trim().indexOf("=") !== -1) {
          }
        }
        var tempcon = "";
        if (con.length > 0) {
          tempcon = tempcon + "概念+=";
          for (let m = 0; m < con.length; m++) {
            tempcon = tempcon + con[m] + " ";
          }
        }
        var temprel = "";
        if (rel.length > 0) {
          temprel = temprel + "关系+=";
          for (let n = 0; n < rel.length; n++) {
            temprel = temprel + rel[n] + " ";
          }
        }
        var uploadData = [];
        uploadData.push(tempcon);
        uploadData.push(temprel);

        diff.forEach(element => {
          uploadData.push(element);
        });
      }
      console.log(uploadData);
      let res = await submitKgCode({
        dbname: this.selectKGDBName,
        seqs: uploadData,
      });
      console.log(res);
    },
    // 过滤出单独节点
    filtIndividualNodes(nodes, edges) {
      var relNodes = [];
      var IndividualNodes = [];
      for (let i = 0; i < edges.length; i++) {
        const edge = edges[i];
        relNodes.push(edge.from);
        relNodes.push(edge.to);
      }
      for (let i = 0; i < relNodes.length; i++) {
        for (let j = 0; j < nodes.length; j++) {
          if (relNodes[i] === nodes[j].id) {
            nodes.split(j, 1);
          }
        }
      }
      IndividualNodes = nodes;
      return IndividualNodes;
    },
  // 新建数据库
  // cid字段
    async addKGDB() {
      // 添加库
      if (this.addKGDBName != "") {
        // let res = await createKGDB({
        //   dbname: 'KG_cid',
        // });
        // 添加库关联的课程名库
        let info = await addADBInfoInter({
          dbname: "KG_" + "cid",
          cname: this.addKGDBName,
          cid: "202111271303",
        });
        this.$q.notify({
          message: `添加成功！`,
          timeout: 300,
          color: "green",
          position: "top",
        });
        this.addKGDBDialog = false;
      }
    },
    // 模糊匹配
    async matchSearchByStr() {
      console.log(this.uploadDBName);
      let res = await matchSearch({
        dbname: this.uploadDBName,
        headword: this.searchTerm,
        type: "con",
      });
      console.log(res.data.data);
    },
    // 是否存在课程名字的数据库
    // cid字段
    async checkExistDB(selectKGDBName) {
      console.log(selectKGDBName);
      let res = await checkExistDBInter({
        name: selectKGDBName,
        // 输入cid
        cid: "202111271003",
      });
      // 不存在
      if (res.data.data === null) {
        // 添加
        this.addKGDBDialog = true;
        this.addKGDBName = this.selectKGDBName;
      } else {
        // 存在则将上传的数据库的名字保存下来
        this.uploadDBName = res.data.data.dbname;
      }
    },
  },
  async mounted() {
    // 初始化图谱容器
    this.container = document.getElementById("mynetwork");
    this.createGraphInit({
      graph: {
        nodes: [],
        edges: [],
      },
    });
    // 初始化图谱数据库名称
    let res = await findAllDBNameAndCName();
    let info = res.data.data;
    for (let i = 0; i < info.length; i++) {
      const item = info[i];
      this.DBList.push(item.course.name);
    }
  },
};
</script>

<style scoped>
#particles-js {
  position: absolute !important;
  width: 100vw !important;
  height: 100vh !important;
  background-repeat: no-repeat !important;
  background-size: cover !important;
  background-position: 50% 50% !important;
}
.normal_gradient {
  background: linear-gradient(145deg, rgb(74, 94, 137) 15%, #b61924 70%);
}
.dark_gradient {
  background: linear-gradient(145deg, rgb(11, 26, 61) 15%, #4c1014 70%);
}
.login-form {
  position: absolute;
}
</style>

<style lang="sass">
.GPLAY
  &__toolbar
    height: 60px
  &__logo
    width: 183px
    height: 39px
  &__toolbar-input-container
    min-width: 100px
    width: 55%
  &__toolbar-input-btn
    border-radius: 0
    max-width: 60px
    width: 100%
  &__drawer-link
    .q-item__section--avatar
      margin: -8px 0 -8px -16px
      padding: 8px 0 8px 16px
    .q-item__section--main
      margin: -8px -16px -8px 16px
      padding: 8px 16px 8px 2px
      font-size: 18px
      font-weight: 300
    &--apps, &--movies, &--music, &--books, &--devices
      background: #f5f5f5!important
      &:hover
        color: #eee !important
    &--apps:hover
      background: #43a047!important
    &--movies:hover
      background: #e53935!important
    &--music:hover
      background: #fb8c00!important
    &--books:hover
      background: #1e88e5!important
    &--devices:hover
      background: #546e7a!important
  &__drawer-item
    padding: 6px 12px 6px 23px
  &__sticky
    min-height: 49px
    border-bottom: 1px solid rgba(0,0,0,0.12)
  &__sticky-help
    border: 1px solid #ccc
    padding-left: 8px
    padding-right: 8px
  &__sticky-settings
    padding-left: 17px
    padding-right: 17px 
    border: 1px solid #ccc
</style>
