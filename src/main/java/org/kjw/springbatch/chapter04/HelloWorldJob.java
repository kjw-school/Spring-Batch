package org.kjw.springbatch.chapter04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
/**
 * 스프링 배치는 JobParameter 클래스의 인스턴스에 잡 파라미터를 저장하는데, getJobParameters()를 호출하는 방식으로 잡 파라미터를 가져오면 Map<String, Object>가 반환된다.
 */
public class HelloWorldJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job2() {
		return this.jobBuilderFactory.get("basicJob")
			.start(step2())
			.build();
	}

	@Bean
	public Step step2() {
		return this.stepBuilderFactory.get("step1")
			.tasklet(helloWorldTasklet())
			.build();
	}

	@Bean
	public Tasklet helloWorldTasklet() {
		return (contribution, chunkContext) -> {
			String name = (String)chunkContext.getStepContext().getJobParameters().get("name");
			System.out.println(String.format("Hello, %s!", name));
			return RepeatStatus.FINISHED;
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldJob.class, args);
	}
}
