import styled from "styled-components";

export const MyProfileContainer = styled.div`
  width: 300px;
  background-color: var(--gray-100);
  border-radius: 10px;
  padding: 16px 16px 32px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
export const ProfileWrapper = styled.div`
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
export const ProfilePhoto = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;

  &.small_logoface {
    opacity: 70%;
  }
`;
export const ProfileName = styled.div`
  padding-top: 16px;
`;
