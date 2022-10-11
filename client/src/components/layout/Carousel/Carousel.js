import { useState, useEffect } from "react";
import logoface from "../../../assets/small_logoface.png";
import { RecipeWrapper, Image, Name, Container, StyledFontAwesomeIcon, StyledSlider } from "./CarouselStyle";
import { faChevronRight, faChevronLeft } from "@fortawesome/free-solid-svg-icons";

// react-slick 관련
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { Link } from "react-router-dom";

const Carousel = ({ recipes }) => {

  // 바로 slidesToShow의 값에 Math.min을 주는 경우 초기 값이 0이 되어
  // Warning: `Infinity` is an invalid value for the `width` css style property.
  // 라는 에러 메세지가 나온다. 상태를 사용하여 초기 값을 4와 2로 고정시킨다.
  const [ recipeLength, setRecipeLength ] = useState(4);
  const [ recipeLengthMobile, setRecipeLengthMobile ] = useState(2);

  useEffect(() => {
    if (recipes.length > 0) {
      setRecipeLength(Math.min(recipes.length, 4)) // 데이터의 양이 4보다 적은 경우 적은 수를 slidesToShow로 지정한다
      setRecipeLengthMobile(Math.min(recipeLength, 2)) // 데이터의 양이 2보다 적은 경우 적은 수를 slidesToShow로 지정한다
    }
  }, [recipes])

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
    slidesToShow: recipeLength, 
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
          slidesToShow: recipeLengthMobile,
          slidesToScroll: 1,
          initialSlide: 2,
          arrows: false,
          dots: true,
        }
      }
    ]
  };

  // 보여줄 수 있는 내용이 4개 이하일 때 레시피가 가운데 올 수 있도록 props로 carousel 길이 조정
  const classNameMaker = () => {
    if (recipes.length === 3) {
      return "600px";
    } 
    else if (recipes.length === 2) {
      return "400px";
    }
    else if (recipes.length === 1) {
      return "180px";
    } 
  }

  return (
    <Container>
      <StyledSlider {...settings} width={classNameMaker()}>
        {recipes.map((el, idx) => {
          return (
            <RecipeWrapper key={idx}>
              <Link to={"/recipes/" + el.id}>
                <Image src={el.imagePath} alt={el.title} />
                <Name>{el.title}</Name>
              </Link>
            </RecipeWrapper>
          )
        })}
      </StyledSlider>
    </Container>
  )
}

export default Carousel;