import axios from "axios";
import logoface from "../../assets/small_logoface.png";
import { useEffect } from "react";
import { Container, Header, Image, Name, RecipeWrapper } from "./HomeStyle";
import Carousel from "../../components/layout/Carousel/Carousel";
import IntroCarousel from "../../components/layout/Carousel/IntroCarousel";

const Home = () => {

  const dummyData = [{
    img: "https://mblogthumb-phinf.pstatic.net/MjAxNzA4MDRfMjQ2/MDAxNTAxODI4MTE3MjAz.TsMIHlbXW988cxCpFfALZYiOriewTcCvRt_MbLh53-Ug.UFBMekEe6fVE64VXmdl6IJtji1KP9F2ybmz652fdSDAg.JPEG.drea_min_g/%EB%9D%BC%EB%A9%B4%EC%9A%94%EB%A6%AC_%EB%9D%BC%EB%A9%B4%EC%83%89%EB%8B%A4%EB%A5%B4%EA%B2%8C_%EB%9D%BC%EB%A9%B4%EC%83%89%EB%8B%A4%EB%A5%B4%EA%B2%8C%EB%A8%B9%EA%B8%B0_%EB%9D%BC%EB%A9%B4%EB%A0%88%EC%8B%9C%ED%94%BC_%EC%BF%A0%EC%A7%80%EB%9D%BC%EC%9D%B4.jpg?type=w2", 
    name: "맛있는 김치볶음밥"
  }, {
    img: "https://img1.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/onehomelife/20210525054808865jjfg.png", 
    name: "백종원의 떡국 레시피"
  }, {
    img: "https://image.fmkorea.com/files/attach/new/20200306/3655304/1202549011/2793291225/03d202790c35c6d4e3c6eb98396bfe8b.jpeg", 
    name: "자취생 닭볶음탕 레시피"
  }, {
    img: "https://t1.daumcdn.net/cfile/tistory/9967363D5B28F79708", 
    name: "초간단 라구 파스타"
  }, {
    img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTyefT-EwgPRPIRVbGp58bNpvQun_35H5wfLA&usqp=CAU", 
    name: "특별한 날엔 간장계란밥"
  }, {
    img: "https://i.ytimg.com/vi/6DrdVtK14fQ/maxresdefault.jpg", 
    name: "10분만에 만드는 순두부찌개"
  }, {
    img: "https://mblogthumb-phinf.pstatic.net/MjAxNzAxMTdfMTY3/MDAxNDg0NjQwNzkwMjIw.ZJ63p2tv6YcQGf7k1X-KFO9Yaf6w3Wwwzwhi5fIyNNcg.yRwxxx_z-Ei7SndsMqCjGgP0hqk0kIlzap3lNKRtBLQg.JPEG.vanessab/%EC%9E%90%EC%B7%A8%EC%9A%94%EB%A6%AC20.JPG?type=w2", 
    name: "세가지 재료로 만드는 피자"
  },{
    img: "https://cphoto.asiae.co.kr/listimglink/6/2021050313164819330_1620015408.jpg", 
    name: "군고구마 그라탕"
  }]

  return (
    <Container>
      <IntroCarousel />
      <Header>추천 레시피</Header>
      <Carousel dummyData={dummyData} />
      <Header>인기 레시피</Header>
      <Carousel dummyData={dummyData} />
    </Container>
  );
};

export default Home;
