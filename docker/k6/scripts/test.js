import http from 'k6/http';
import { check, sleep } from 'k6';

// VU/Duration 설정
export const options = {
  vus: 1000,
  duration: '1m',
};

// 랜덤 유틸
function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

const sorts = ['LIKES_ASC', 'PRICE_ASC', 'LATEST'];

  const brandId = randomInt(2001, 2200);
  const sort = sorts[randomInt(0, sorts.length - 1)];
  const page = randomInt(1, 6);
  const limit = 20;

export default function () {


  const url = `${__ENV.BASE_URL}/api/v1/products?brandId=${brandId}&sort=${sort}&page=${page}&limit=${limit}`;

  const res = http.get(url, {
    headers: {
      'Content-Type': 'application/json',
    },
    tags: { name: 'GET /api/v1/products' },
  });

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}