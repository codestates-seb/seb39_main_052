import { useState, useRef } from "react";
import { useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import blankImage from "../../../assets/blankImage.png";
import { addImage, addMainImage, deleteImage, deleteMainImage } from "../../../features/imageSlice";
import { setMainImage, setStepImage, editMainImage, editStepImage, deleteMainImg, deleteStepImage } from "../../../features/recipeSlice";
import { faSpinner, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, Img, Input, Button, StyledFontAwesomeIcon } from "./ImageUploaderStyle";

const ImageUploader = ({ size, index, mode }) => {

    const [imageUrl, setImageUrl] = useState(null); //미리보기용
    const [isLoading, setIsLoading] = useState(false);
    const [isDragOver, setIsDragOver] = useState(false);

    const { pathname } = useLocation();
    const dispatch = useDispatch();

    const imgRef = useRef(); // input 내장 버튼을 직접 만든 버튼과 연결하기 위한 ref

    // 메인 이미지 url
    const mainImageUrl = useSelector((state) => {
        return state.recipe.imageInfo.imagePath;
    }); 
    // console.log("mainImageUrl", mainImageUrl);
    // 요리 순서 상태
    const steps = useSelector((state) => {
        return state.recipe.steps;
    }); 
    // console.log("steps", steps);

    // 이미지 핸들러 (클릭시/드랍시 모두 가능)
    const imageHandler = (fileList) => {
        setIsLoading(true);
        setIsDragOver(false);

        if (fileList[0]) {
            const maxSize = 2 * 1024 * 1024;
            const uploadFile = fileList[0]
            
            // 용량 2MB로 제한
            if (uploadFile.size > maxSize) {
                setIsLoading(false);
                alert("이미지 사이즈는 2MB 이내로만 등록할 수 있어요ㅠㅠ")
                return false;
            }
            else {
                // 레시피 작성하기
                if (pathname === "/recipes/new") {
                    // 메인 사진을 추가하는 경우
                    if (mode === `main`) {
                        dispatch(addMainImage({ image: uploadFile }));
                        dispatch(setMainImage({ mainImage: URL.createObjectURL(uploadFile)}));
                    }
                    // 요리 순서 사진을 추가하는 경우
                    if (mode === `steps`) {
                        dispatch(addImage({ index: index, image: uploadFile }));
                        dispatch(setStepImage({ index: index, imagePath: URL.createObjectURL(uploadFile)}))
                    }
                }
                // 레시피 수정하기
                else {
                    if (mode === `main`) {
                        dispatch(addMainImage({ image: uploadFile }));
                        dispatch(editMainImage({ mainImage: URL.createObjectURL(uploadFile)}));
                    }
                    // 요리 순서 사진을 추가하는 경우
                    if (mode === `steps`) {
                        dispatch(addImage({ index: index, image: uploadFile }));
                        dispatch(editStepImage({ index: index, imagePath: URL.createObjectURL(uploadFile)}))
                    }
                }
            }

        }
        else {
            setIsLoading(false);
        }
    };

    const handleClick = async() => {
        // 버튼 클릭으로 input 클릭과 동일한 기능을 한다.
        await imgRef.current.click();
    };

    const handleDelete = () => {
        setImageUrl(null);
        imgRef.current.value="";
        // 메인 사진을 지우는 경우
        if (mode === `main`) {
            dispatch(deleteMainImage());
            dispatch(deleteMainImg());
        }
        //요리 순서 사진을 지우는 경우
        if (mode === `steps`) {
            dispatch(deleteImage({ index: index }));
            dispatch(deleteStepImage({ index: index }))
        }
    }

    // 클릭으로 이미지 파일 추가
    const onClickFiles = (e) => {
        // console.log(e.target.files);
        e.preventDefault()
        imageHandler(e.target.files);
    };

    // drop으로 이미지 파일 추가
    const onDropFiles = (e) => {
        // console.log(e.dataTransfer.files);
        e.preventDefault()
        imageHandler(e.dataTransfer.files);
    };

    // 없으면 drop 작동안함
    const dragOver = (e) => {
        e.preventDefault()
        setIsDragOver(true);
    };


    return (
        <Container className={size}>
            {mode === `main`
                ? <Img
                    src={mainImageUrl ? mainImageUrl : blankImage}
                    onDrop={onDropFiles}
                    onDragOver={dragOver} 
                    onLoad={() => setIsLoading(false)}
                    className={isDragOver && "dragover"}
                />
                : <Img
                    src={steps[index].imageInfo.imagePath ? steps[index].imageInfo.imagePath : blankImage}
                    onDrop={onDropFiles}
                    onDragOver={dragOver} 
                    onLoad={() => setIsLoading(false)}
                    className={isDragOver && "dragover"}
                />
            }
            {isLoading && <StyledFontAwesomeIcon icon={faSpinner} className="loading" spin />}
            <Input
                type="file"
                accept="image/*"
                name="file"
                ref={imgRef}
                onChange={onClickFiles}
            />
            {mode === "main"
                ? (mainImageUrl && !isLoading
                    ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                    : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick} />
                )
                : (steps[index].imageInfo.imagePath && !isLoading
                    ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                    : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick} />
                )
            }
        </Container>
    )
};

export default ImageUploader;