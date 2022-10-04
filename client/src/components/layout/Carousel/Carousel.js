import logoface from "../../../assets/small_logoface.png";
import { RecipeWrapper, Image, Name, Container, StyledFontAwesomeIcon, StyledSlider } from "./CarouselStyle";
import { faChevronRight, faChevronLeft } from "@fortawesome/free-solid-svg-icons";
// react-slick 관련
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const Carousel = ({ dummyData }) => {

  // 왼쪽 화살표
  const NextArrow = (props) => {
    const { className, style, onClick } = props;
    return (
      <div
        className={className}
        style={{ ...style, display: "block" }}
        onClick={onClick}
      >
        <StyledFontAwesomeIcon icon={faChevronRight} />
      </div>
    );
  }
  // 오른쪽 화살표
  const PrevArrow = (props) => {
    const { className, style, onClick } = props;
    return (
      <div
        className={className}
        style={{ ...style, display: "block" }}
        onClick={onClick}
      >
        <StyledFontAwesomeIcon icon={faChevronLeft} />
      </div>
    );
  }
  
 
  // react-slick 세팅 변수
  const settings = {
    dots: false,
    infinite: true,
    autoplay: true,
    speed: 500,
    autoplaySpeed: 2000,
    cssEase: "linear",
    slidesToShow: Math.min(dummyData.length, 4), // 데이터의 양이 4보다 적은 경우 적은 수를 slidesToShow로 지정한다
    slidesToScroll: 1,
    swipeToSlide: true,
    initialSlide: 0,
    pauseOnHover: true,
    nextArrow: <NextArrow />,
    prevArrow: <PrevArrow />,
    responsive: [
      {
        breakpoint: 600,
        settings: {
          slidesToShow: Math.min(dummyData.length, 2),
          slidesToScroll: 1,
          initialSlide: 2,
          arrows: false,
          dots: true,
        }
      }
    ]
  };

  // 보여줄 수 있는 내용이 4개 이하일 때 carousel 길이 조정
  const classNameMaker = () => {
    if (dummyData.length === 3) {
      return "600px";
    } 
    else if (dummyData.length === 2) {
      return "400px";
    } 
  }

  return (
    <Container>
      <StyledSlider {...settings} width={classNameMaker()}>
        {dummyData.map((el, idx) => {
          return (
            <RecipeWrapper key={idx}>
              <Image src={el.img} alt={el.name} />
              <Name>{el.name}</Name>
            </RecipeWrapper>
          )
        })}
      </StyledSlider>
    </Container>
  )
}

export default Carousel