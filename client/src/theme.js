const deviceSize = {
  minMobile: "360px",
  mobile: "600px",
  laptop: "1024px",
};

const device = {
  minMobile: `screen and (min-width: ${deviceSize.minMobile})`,
  mobile: `screen and (max-width: ${deviceSize.mobile})`,
  laptop: `screen and (min-width: ${deviceSize.laptop})`,
};

const theme = {
  device,
};

export default theme;
