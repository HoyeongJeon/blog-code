const human = {
	name: "홍길동",
	home: {
		city: "서울",
		address: "강남구",
	},
};

const zipcode = human.home.zipcode;
console.log(zipcode);

console.log(human.notExistsMethod?.());
