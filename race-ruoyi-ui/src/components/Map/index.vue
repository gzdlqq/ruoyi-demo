<template>
  <div>
    <div  id="searchContainer">
      <input id="place" type="text" placeholder="请输入地址" />
      <div id="searchResults"></div>
    </div>
    <div id="map" style="height: 600px;"></div>
  </div>
</template>


<script>

export default {
  name: 'Map',
  props: {
    form: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      map: null,
      center: null,
      marker: null,
      address: '',
      keyword: ''
    }
  },
  mounted() {
    this.initMap()
  },
  methods: {
    search() {

    },
    initMap() {

      let _this=this
      //默认值写法
      let lat = this.form.latitude || '39.984120'
      let long =this.form.longitude || '116.307484'



      this.center = new qq.maps.LatLng(lat, long);

      // 创建地图容器
      this.map = new qq.maps.Map(document.getElementById("map"), {
        center: this.center,
        zoom: 13
      })

      //标记点
      // 创建初始标记对象，并设置其位置
      this.marker = new qq.maps.Marker({
        position: this.center,
        map: this.map
      });





      // 绑定地图点击事件
      qq.maps.event.addListener(this.map, 'click', (event) => {
        // 获取点击坐标经纬度
        const latLng = event.latLng;

        //新的中心
        _this.center = new qq.maps.LatLng(latLng.lat, latLng.lng);
        //新的标记  , 直接修改原来的标记
        _this.marker.setPosition(latLng);


        //经纬度逆地址解析 对象定义
        var geocoder = new qq.maps.Geocoder({
          complete: function(result){
            // debugger
            console.log('地址:'+result.detail.address); // 打印详细地址信息
            _this.address = result.detail



            // 获取经纬度， 获取地址
            _this.$emit('chageJingweidu',event,_this.address)
          }
        });


        //逆解析地址
        geocoder.getAddress(this.center);

      })


      //搜索功能
      // JavaScript
      var place = document.getElementById('place');
      var ap = new qq.maps.place.Autocomplete(place);
      var searchResults = document.getElementById('searchResults');
      var keyword = "";
      var searchService = new qq.maps.SearchService({
        complete : function(results){
          debugger
          // 清空搜索结果
          searchResults.innerHTML = "";
          if (results.type === "CITY_LIST") {
            searchService.setLocation(results.detail.cities[0].cityName);
            searchService.search(keyword);
            return;
          }
          var pois = results.detail.pois;
          var latlngBounds = new qq.maps.LatLngBounds();
          for (var i = 0, l = pois.length; i < l; i++) {
            var poi = pois[i];
            latlngBounds.extend(poi.latLng);
            var marker = new qq.maps.Marker({
              map: _this.map,
              position: poi.latLng
            });
            marker.setTitle(poi.name);
            // 将搜索结果添加到提示框中
            var resultItem = document.createElement('div');
            resultItem.innerText = poi.name;
            resultItem.classList.add('result-item');
            resultItem.addEventListener('click', function() {
              place.value = poi.name;
              searchResults.style.display = 'none';
              // 在这里可以根据需要执行其他操作
            });
            searchResults.appendChild(resultItem);
          }
          _this.map.fitBounds(latlngBounds);
          // 显示搜索结果提示框
          searchResults.style.display = 'block';
        }
      });

// 添加输入框的输入事件监听器，用于在输入时隐藏搜索结果提示框
// 添加输入框的输入事件监听器，用于实现实时搜索
      place.addEventListener('input', function() {
        keyword = place.value;
        searchService.search(keyword);
      });

// 添加输入框的焦点事件监听器，用于在输入框获得焦点时显示搜索结果提示框
      place.addEventListener('focus', function() {
        searchResults.style.display = 'block';
      });

// 添加输入框的失去焦点事件监听器，用于在输入框失去焦点时隐藏搜索结果提示框
      place.addEventListener('blur', function() {
        setTimeout(function() {
          searchResults.style.display = 'none';
        }, 200);
      });

      // //添加监听事件
      qq.maps.event.addListener(ap, "confirm", function(res){
        keyword = res.value;
        searchService.search(keyword);
      });


      document.getElementById('searchResults').addEventListener('click', function(event) {
        // Check if a li element was clicked
        if (event.target.tagName === 'DIV') {
          // Get the clicked search result
          var clickedResult = event.target.textContent;
          alert(clickedResult)
          // Fill the clicked result into the input element
          document.getElementById('place').value = clickedResult;
        }
      });
    }
  }
}
</script>


<style scoped>
#searchContainer {
  position: relative;
}

#place {
  width:200px;
  padding: 10px;
  font-size: 16px;
  border: none;
  border-bottom: 2px solid red;
  outline: none;
}

#searchResults {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border-top: none;
  max-height: 200px;
  overflow-y: auto;
  padding: 10px;
  display: none;
  z-index: 999;
}

#searchResults.active {
  display: block;
}
</style>
