import { Button, Container, Image, Text, Left, Right, StyledSlider, Banner } from "./IntroCarouselStyle";
import firstImage from "../../../assets/food.png";
import secondImage from "../../../assets/laptop_n_phone.png";
import { Link } from "react-router-dom";

// react-slick 관련
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const IntroCarousel = () => {
    // react-slick 세팅 변수
    const settings = {
        dots: true,
        infinite: true,
        autoplay: true,
        speed: 500,
        autoplaySpeed: 5000,
        cssEase: "linear",
        slidesToShow: 1,
        slidesToScroll: 1,
        swipeToSlide: true,
        initialSlide: 0,
        pauseOnHover: true,
        useTransform: false, // 마지막 페이지 > 첫페이지 전환시 깜박임 문제 해결
    }

    return (
        <Banner>
            <StyledSlider {...settings}>
                <Container className="mint">
                    <Left>
                        <Text>
                            <p>내 <span>냉장고</span> 속 <span>재료</span>만으로</p>
                            <p>만들 수 있는 <span>요리</span>가 궁금해?</p>
                        </Text>
                        <Link to="/search">
                            <Button><strong>냉장고 파먹기</strong>&nbsp;바로가기</Button>
                        </Link>
                    </Left>
                    <Right>
                        <Image src={firstImage} alt="a cooking man" />
                    </Right>
                </Container>
                <Container className="pink">
                    <Left>
                        <Text className="pink">
                            <p>자취생의 <span>모바일 냉장고&nbsp;</span></p>
                            <p>어디서든 내 냉장고를 확인해봐!</p>
                        </Text>
                        <Link to="/myfridge">
                            <Button className="pink"><strong>나의 냉장고</strong>&nbsp;바로가기</Button>
                        </Link>
                    </Left>
                    <Right>
                        <Image src={secondImage} alt="a cooking man" className="small" />
                    </Right>
                </Container>
            </StyledSlider>
        </Banner >
    )
}

export default IntroCarousel